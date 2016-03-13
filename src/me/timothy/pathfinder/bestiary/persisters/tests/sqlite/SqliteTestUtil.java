package me.timothy.pathfinder.bestiary.persisters.tests.sqlite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SqliteTestUtil {
	static Path getTestPath() {
		return Paths.get(System.getProperty("user.home"), "Documents", "My Games", "Pathfinder", "pathfinder_test.db");
	}
	
	static void checkTestPath() throws IOException {
		Path dbPath = getTestPath();
		
		if(Files.exists(dbPath)) {
			int counter = 1;
			Path newPath;
			do {
				String append = ".old" + (counter > 1 ? counter : "");
				newPath = Paths.get(dbPath.getParent().toString(), dbPath.getFileName() + append);
				counter++;
			}while(Files.exists(newPath));
			
			Files.move(dbPath, newPath);
		}
	}

	public static void deleteTestPath() throws IOException {
		Path dbPath = getTestPath();
		Files.delete(dbPath);
	}
}
