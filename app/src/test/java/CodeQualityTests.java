// This work is licensed under a CC BY 4.0 license. https://creativecommons.org/licenses/by/4.0/

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
// import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CodeQualityTests {
  final static String checkStyleXmlFile = "./build/reports/checkstyle/main.xml";
  final static String findBugsXmlFile = "./build/reports/spotbugs/spotbugs.xml";
  final static String findBugsXmlBackupFile = "./build/reports/spotbugs/spotbugs_bak.xml";
  final static String codeQualityJSONFile = "./build/reports/gl-code-quality-report.json";
  final static String checkStyleJUnitFile = "./build/test-results/TEST-checkstyle.xml";
  final static String findBugsJUnitFile = "./build/test-results/TEST-findbugs.xml";
  final static int maxQualityErrors = 5;
  final static String srcRoot = "src/main/java";  // set this accordingly
  final static String buildRoot = "build/classes/java/main";  // set this accordingly
  final static boolean deleteFindBugsXmlFile = true; // deletes the findbugs file after parsing, this is a fix to prevent bad xml in the file

  static class TestCase {
    String name;
    String className; // this is what gitlab presents as the suite column
    String fileName;
    ArrayList<Failure> failures = new ArrayList<>();
  }

  static class Failure {
    String message = "";
    String type = "";
    String text = "";
  }

  @Test
  public void codeQuality() {
    int errors = 0;
    errors = checkStyleTest();


    // for some reason the findbugs xml can be corrupted and not parse
    // we create a complex system of backups to use in that case...
    // for some reason the findbugs xml is also not regenerated if it is not there in about 50% of the cases
    // odd
    Path findBugsXML = Paths.get(findBugsXmlFile);
    Path findBugsXMLBackup = Paths.get(findBugsXmlBackupFile);
    if (hasFindBugsFile(findBugsXML, findBugsXMLBackup)) {
      errors += findBugsTest();
      if (deleteFindBugsXmlFile) {
        createBackup(findBugsXML, findBugsXMLBackup);
        
      }
    }

    assertTrue(errors < maxQualityErrors, "Max amount (" + maxQualityErrors +") of quality issues exceeded:" + errors);
  }

  private boolean createBackup(Path original, Path backup) {
    try {
      Files.move(original, backup, StandardCopyOption.REPLACE_EXISTING );   
    } catch (Exception e) {
      System.out.println("Could not create backup: " + original.toString() + " -> " + backup.toString());
      
      if (deleteFindBugsXmlFile) {
        deleteFindBugsXmlFile();
        System.out.println("Running subsequent builds without code changes could be problematic.");
      } else {
        System.out.println("There is a high chance that the original xml will be corrupted: " + original.toString() + " (You could consider deleting it manually)");
      }
    }
    return true;
  }

  private boolean hasFindBugsFile(Path original, Path backup) {
    if (!Files.exists(original)) {
      System.err.println("Findbugs xml file not found: " + original.toString());
      System.err.println("Consider making code change to initiate regeneration of the xml (will try backup)");
      try {
        Files.move(backup, original, StandardCopyOption.REPLACE_EXISTING );
        System.err.println("Restored xml from backup: " + backup.toString());
      } catch (Exception e) {
        System.err.println("Could not find or use the findbugx xml backup: " + backup.toString());
        System.err.println("Aborting findbugs xml to JUnit xml conversion...");
        return false;
      }
    }

    return true;
  }

  private void deleteFindBugsXmlFile() {
    try {
      Files.deleteIfExists(Paths.get(findBugsXmlFile));
      System.out.println("Deleted file: " + findBugsXmlFile);
    } catch (IOException e) {
      System.out.println("Could not delete findbugs XML: " + findBugsXmlFile);
      System.out.println("You may need to do this manually...");
    }
  }

  public int findBugsTest() {
    DocumentBuilder dBuilder = null;
    int errors = 0;
    try {
      dBuilder = getDocumentBuilder();
      Document doc = dBuilder.parse(new FileInputStream(findBugsXmlFile));
      doc.getDocumentElement().normalize();

      // first we collect all bug patterns
      HashMap<String, String> bugPatterns = new HashMap<>();
      NodeList bpNodes = doc.getElementsByTagName("BugPattern");
      for (int bpIx = 0; bpIx < bpNodes.getLength(); bpIx++) {
        Node bpNode = bpNodes.item(bpIx);
        String type =  bpNode.getAttributes().getNamedItem("type").getTextContent();
        String details = fixBugPatternText(bpNode.getTextContent().trim());
        bugPatterns.put(type, details);
      }

      // then we check all bug instances and collect them per file
      HashMap<String, TestCase> bugInstances = new HashMap<>();

      // we should actually add all the checked files first so we can get some passing tests too
      NodeList classNodes = doc.getElementsByTagName("FileStats");
      for (int cnIx = 0; cnIx < classNodes.getLength(); cnIx++) {
        String fileName = classNodes.item(cnIx).getAttributes().getNamedItem("path").getNodeValue();
        TestCase tc = new TestCase();
        tc.fileName = fileName;
        tc.className = "FindBugs Issues";
        tc.name = fileName;
        bugInstances.put(fileName, tc);
      }

      NodeList biNodes = doc.getElementsByTagName("BugInstance");
      for (int biIx = 0; biIx < biNodes.getLength(); biIx++) {
        Element biNode = (Element)biNodes.item(biIx);

        String path = biNode.getElementsByTagName("SourceLine").item(0).getAttributes().getNamedItem("sourcepath").getTextContent();
        String longMessage = biNode.getElementsByTagName("LongMessage").item(0).getTextContent();
        String className = biNode.getElementsByTagName("Class").item(0).getAttributes().getNamedItem("classname").getTextContent() + ".";
        longMessage = longMessage.replace(className, "");
        String line = biNode.getElementsByTagName("SourceLine").item(0).getAttributes().getNamedItem("start").getTextContent();
        line += "-" + biNode.getElementsByTagName("SourceLine").item(0).getAttributes().getNamedItem("end").getTextContent();
        String type = biNode.getAttribute("type");


        TestCase tc = bugInstances.get(path);
        if (tc == null) {
          System.err.println("Could not find bug instance for:"  + path);
        } else {
          Failure f = new Failure();
          tc.failures.add(f);


          f.type = "FindBugs Issue";
          f.message = "FindBugs Issues";

          f.text += "lines: " + line + System.lineSeparator() + longMessage + System.lineSeparator() + bugPatterns.get(type);
        }

        errors++;
      }

      saveTestCasesAsXML(bugInstances.values(), findBugsJUnitFile, "org.spotbugs", "findbugs");
      reportTestCaseToConsole(bugInstances.values());

    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      assertTrue(false, "File not found: " + findBugsXmlFile);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      //e.printStackTrace();
      System.out.println("XML parsing problem in: " + findBugsXmlFile + " (I will try to delete the file and you can then run the build again...)");
      deleteFindBugsXmlFile();
      assertTrue(false, "XML parsing problem in: " + findBugsXmlFile + " (You could try deleting the file and running again...)");
    }

    return errors;
  }

  private void reportTestCaseToConsole(Collection<TestCase> values) {
    for(TestCase t : values) {
      System.out.println(t.failures.size() + " " + t.className + " in " + t.fileName);
      for (Failure f : t.failures) {
        System.out.println("text:" + fixStringLength(f.text, 100) + System.lineSeparator());
      }
    }
  }

  private String fixBugPatternText(String str) {

    // we can treat this text as an hmtl (xml) document to and do the rendering based on this...
    try {
      DocumentBuilder dBuilder = null;
      dBuilder = getDocumentBuilder();
      String htmlstr = "<html>" + str + "</html>";

      // this xml parser does not handle html enties like: &nbsp;
      htmlstr = htmlstr.replace("&nbsp;", " "); // for some reason it seems the parser does not want to handle the nbsp
      htmlstr = htmlstr.replace("&amp;", "&");
      
      // oddly it handles these... maybe part of the xml standards...
      //htmlstr = htmlstr.replace("&lt;", "<");
      //htmlstr = htmlstr.replace("&gt;", ">");
    
      Document doc = dBuilder.parse(new ByteArrayInputStream(htmlstr.getBytes()));
      doc.getDocumentElement().normalize();

      NodeList nodes = doc.getFirstChild().getChildNodes(); // first child is html tag so we go directly to the children of this node and render them

      String out = "";
      for (int i = 0; i < nodes.getLength(); i++) {
        out += getHTMLNodeText(nodes.item(i));
      }
      return out;

    } catch (ParserConfigurationException | SAXException | IOException e) {
      System.err.println(e.getMessage());
    }

    System.err.println("parsing of text failed possibly due to bad html/xml formatting for, start text --->" +  str + "<--- end text");
    // parsing seem to have failed so we revert so some crappy replacements instead...
    str = str.replace("    ", "\t");
    str = str.replace("\n    ", " ");
    // removes some select html tags
    final String[] remove = new String[] {"<p>", "</p>", "<code>", "</code>", "<pre>", "</pre>"};
    String ret = str;
    for (String r : remove) {
      ret = ret.replace(r, "");
    }

    return ret;
  }

  private String getHTMLNodeText(Node item) {
    if (item.getNodeName() == "pre") {
      return System.lineSeparator() + item.getTextContent();
    } else if (item.getNodeName() == "br") {
      return System.lineSeparator();
    }

    String text = item.getTextContent();
    text = text.replace("\n", " ");
    while(text.contains("  ")) {
      text = text.replace("  ", " ");
    }

    text = text.trim();
    return item.getNodeName().equalsIgnoreCase("p") ? System.lineSeparator() + text + System.lineSeparator() : text;
  }

  public int checkStyleTest() {
    ArrayList<TestCase> testCases = new ArrayList<>();
    int errors = 0;

    DocumentBuilder dBuilder = null;
    try {
      dBuilder = getDocumentBuilder();
      Document doc = dBuilder.parse(new FileInputStream(checkStyleXmlFile));
      doc.getDocumentElement().normalize();

      NodeList fileNodes = doc.getElementsByTagName("file");

      for (int fnIx = 0; fnIx < fileNodes.getLength(); fnIx++) {
        Node fileNode = fileNodes.item(fnIx);
        String fileName = fileNode.getAttributes().getNamedItem("name").getTextContent();

        TestCase tc = new TestCase();
        testCases.add(tc);
        fileName = fileName.replace('\\', '/');
        tc.name = fileName.substring(fileName.indexOf(srcRoot+  "/") + srcRoot.length() + 1);
        tc.className = "CheckStyle Issues";
        tc.fileName = fileName;

        NodeList childNodes = fileNode.getChildNodes();
        for (int cnIx = 0; cnIx < childNodes.getLength(); cnIx++) {
          Failure f = new Failure();
          f.message = "CheckStyle Issues";
          f.text = "";

          Node childNode = childNodes.item(cnIx);
          if (childNode.getNodeName().equals("error")) {
            String message = childNode.getAttributes().getNamedItem("message").getTextContent();
            String line =  childNode.getAttributes().getNamedItem("line").getTextContent();
            String col =  childNode.getAttributes().getNamedItem("column") != null ? " column:" + childNode.getAttributes().getNamedItem("column").getTextContent() : "";

            f.text += "line: " + line + " column:" + col + System.lineSeparator() + message;
            errors++;
          }

          if (f.text.length() > 0) {
            tc.failures.add(f);
          }
        }

      }

      saveTestCasesAsXML(testCases, checkStyleJUnitFile, "org.checkstyle", "checkstyle");
      reportTestCaseToConsole(testCases);
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      assertTrue(false, "File not found: " + checkStyleXmlFile);
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return errors;
  }

  private String fixSingleStringLength(String str, final int maxLen) {
    str = str.replace("    ", "\t");
    String[] parts = str.split(" ");
    String ret = "";
    int lastLineLength = 0;

    for (String p : parts) {
      if (lastLineLength + p.length() > maxLen) {
        ret += System.lineSeparator();
        lastLineLength = 0;
        p = "  " + p;
      }
      p = p + " ";
      ret += p;
      lastLineLength += p.length();
    }

    return ret.replace("\t", "  ");
  }

  private String fixStringLength(final String str, final int maxLen) {
    String[] parts = str.split("\r\n|\n");
    String ret = "";

    for (String p :parts) {
      String fixed = fixSingleStringLength(p, maxLen);
      if (!fixed.equals(System.lineSeparator())) {
        ret += fixed;
        ret += System.lineSeparator();
      }
    }

    return ret.trim();
  }

  private void saveTestCasesAsXML(Collection<TestCase> testCases, String a_fileName, String suitePackage, String suiteName) throws IOException {

    final String ls = System.lineSeparator();
    FileWriter file = new FileWriter(a_fileName);

    file.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + ls);

    int []errorCount = new int[] {0};

    testCases.forEach(tc -> errorCount[0] += tc.failures.size());

    file.write("<testsuite package=\"" + suitePackage + "\" time=\"0\" tests=\"" + testCases.size() + "\" errors=\"" + errorCount[0] +"\" name=\"" + suiteName + "\">" + ls);

    for (TestCase tc : testCases) {
      file.write("<testcase time=\"0\" name=\"" + tc.name + "\" classname=\"" + tc.className +"\">" + ls);
      if (tc.failures.size() > 0) {

        // combine all messages into one for better printing in gitlab
        file.write("<failure message=\"issues\" ><![CDATA[");
        for (Failure f : tc.failures) {
          file.write(fixStringLength(f.text, 75) +  ls + "----------------" + ls);
        }
        file.write("]]></failure>" + ls);
      }
      file.write("</testcase>");
    }


    file.write("</testsuite>");
    file.flush();
    file.close();

  }

  private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    return dbFactory.newDocumentBuilder();
  }
}
