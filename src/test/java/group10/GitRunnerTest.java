package group10;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GitRunnerTest {
    @Test
    public void testGitRunner_Invalid() {
        assertThrows(GitAPIException.class, () -> GitRunner.cloneRepo("", "", ""));
    }
}
