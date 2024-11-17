package model.persistance;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Interface for reading files.
 */
public interface FileReaderInterface {
  public void readFile(File theFile, model.domain.StuffLendingSystem sls) throws FileNotFoundException;
}
