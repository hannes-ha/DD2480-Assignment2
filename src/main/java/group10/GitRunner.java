package group10;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class GitRunner {
    public static void cloneRepo(String repoURL, String branch, String directory) throws GitAPIException {
        // create new folder to clone repo in
        File repoDir = new File("/build/" + directory);
        repoDir.mkdir();

        Git.cloneRepository()
                .setURI(repoURL)
                .setDirectory(new File("/build/" + directory))
                .setBranch("refs/heads/" + branch)
                .call();
    }
}
