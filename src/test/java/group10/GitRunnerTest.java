package group10;

import org.eclipse.jgit.api.errors.GitAPIException;;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class GitRunnerTest {
    /**
     * Assert throws.
     * Try cloning a repo with invalid parameters: should throw GitAPIException.
     */
    @Test
    public void testGitRunner_Invalid() {
        assertThrows(GitAPIException.class, () -> GitRunner.cloneRepo("", ""));
    }

    /**
     * Assert true.
     * Try cloning an existing repo, and check if the folder has been created.
     * @throws GitAPIException
     */
    @Test
    public void testGitRunner_Positive() throws GitAPIException {
        GitRunner.cloneRepo("https://github.com/hannes-ha/DD2480-Assignment2.git", "main");
        File repoDir = new File("./build");
        assertTrue(repoDir.exists());
    }
}
