package group10;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class GitRunnerTest {
    /**
     * Assert false
     * Create a directory and delete it, should return false when checking if it exists
     */
    @Test
    public void testGitRunner_DeleteDir_Negative() {
        File file = new File("./test3");
        file.mkdir();
        GitRunner.deleteDir(file);
        assertFalse(new File("./test3").exists());
    }

    /**
     * Assert throws.
     * Try cloning a repo with invalid parameters: should throw GitAPIException.
     */
    @Test
    public void testGitRunner_CloneRepo_Invalid() {
        assertThrows(GitAPIException.class, () -> GitRunner.cloneRepo("", "", "test1"));
        GitRunner.deleteDir(new File("./test1"));
    }

    /**
     * Assert true.
     * Try cloning an existing repo, and check if the folder has been created.
     * @throws GitAPIException
     */
    @Test
    public void testGitRunner_CloneRepo_Positive() throws GitAPIException {
        File repoDir = new File("./test2/src");
        GitRunner.cloneRepo("https://github.com/hannes-ha/DD2480-Assignment2.git", "main", "test2");
        assertTrue(repoDir.exists());
        GitRunner.deleteDir(new File("./test2"));
    }
}
