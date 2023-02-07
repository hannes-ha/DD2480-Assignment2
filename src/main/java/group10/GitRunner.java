package group10;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class GitRunner {
    /**
     * Clones a git repository using the given URL and branch name, and puts it
     * all in a folder named "/build"
     * @param repoURL the URL for the repo
     * @param branch the branch name to clone from
     * @throws GitAPIException if clone fails
     */
    public static void cloneRepo(String repoURL, String branch) throws GitAPIException {
        // create new folder to clone repo in (if already exists: delete)
        File repoDir = new File("./build");
        if (repoDir.exists()) deleteDir(repoDir);
        repoDir.mkdir();

        // clone the repo from the given branch
        Git.cloneRepository()
                .setURI(repoURL)
                .setDirectory(new File("./build"))
                .setBranch("refs/heads/" + branch)
                .call();
    }

    /**
     * Deletes an entire directory and all files in it
     * @param dir the directory to delete
     */
    public static void deleteDir(File dir) {
        for (File subFile : dir.listFiles()) {
            if (subFile.isDirectory()) deleteDir(subFile);
            subFile.delete();
        }
    }
}
