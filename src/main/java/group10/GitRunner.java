package group10;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

/**
 * Class for handling running of Git.
 */
public class GitRunner {
    /**
     * Clones a git repository using the given URL and branch name, and puts it
     * all in a folder whose name is given
     * @param repoURL repoURL the URL for the repo
     * @param branch branch the branch name to clone from
     * @param directory name of the folder to create & clone to
     * @throws GitAPIException GitAPIException if clone fails
     */
    public static void cloneRepo(String repoURL, String branch, String directory) throws GitAPIException {
        // create new folder to clone repo in (if already exists: delete)
        File repoDir = new File("./" + directory);
        if (repoDir.exists()) deleteDir(repoDir);
        repoDir.mkdir();

        // clone the repo from the given branch
        Git.cloneRepository()
                .setURI(repoURL)
                .setDirectory(new File("./" + directory))
                .setBranch("refs/heads/" + branch)
                .call();
    }

    /**
     * Deletes an entire directory and all files in it
     * @param dir the directory to delete
     */
    public static void deleteDir(File dir) {
        if (dir.exists()) {
            for (File subFile : dir.listFiles()) {
                if (subFile.isDirectory()) deleteDir(subFile);
                subFile.delete();

            }
            dir.delete();
        }
    }
}
