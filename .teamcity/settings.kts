import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.exec
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot.UserNameStyle.EMAIL

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2019.1"

project {
    params {
        param(
            "env.SPRING_DATASOURCE_URL",
            "jdbc:mysql://mysql/rms?useSSL=false&createDatabaseIfNotExist=true&characterEncoding=utf8&serverTimezone=GMT"
        )
    }
    vcsRoot(Origin)
    vcsRoot(Frontend)
    buildType(BuildFrontend)
    buildType(Build)
    buildType(DeployDoc)
    buildType(Deploy)
}

object BuildFrontend : BuildType({
    name = "Build Frontend"
    vcs {
        root(Frontend)
    }
    steps {
        exec {
            name = "Install Dependencies"
            path = "npm"
            arguments = "install"
        }
        exec {
            name = "Build"
            path = "npm"
            arguments = "run build"
        }
    }
    triggers {
        vcs {
        }
    }
    artifactRules = "+:dist/**"
})

object Build : BuildType({
    name = "Build"
    vcs {
        root(Origin)
    }
    steps {
        gradle {
            name = "Build"
            tasks = "build"
            coverageEngine = idea {
                includeClasses = "cn.edu.zsc.rms.*"
                excludeClasses = arrayOf("*_", "*Test", "*Tests").joinToString("\n")
            }
        }
    }
    triggers {
        vcs {
        }
    }
})

object DeployDoc : BuildType({
    name = "Deploy Doc"
    type = Type.DEPLOYMENT
    dependencies {
        snapshot(Build) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
    }
    vcs {
        root(Origin)
    }
    steps {
        gradle {
            name = "Build & Push Doc Docker Image"
            tasks = "docDockerPushImage"
        }
    }
    triggers {
        vcs {
            branchFilter = "+:<default>"
        }
    }
})

object Deploy : BuildType({
    name = "Deploy"
    type = Type.DEPLOYMENT
    dependencies {
        snapshot(Build) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
        artifacts(BuildFrontend) {
            lastSuccessful()
            cleanDestination = true
            rules = listOf(ArtifactRule.include("**/*", "src/main/jib/app/frontend/dist"))
        }
    }
    vcs {
        root(Origin)
    }
    steps {
        gradle {
            name = "Build & Push Docker Image"
            tasks = "jib"
        }
    }
    triggers {
        vcs {
            branchFilter = "+:<default>"
        }
        finishBuildTrigger {
            buildType = BuildFrontend.id?.value
            successfulOnly = true
            branchFilter = "+:<default>"
        }
    }
})

object Origin : GitVcsRoot({
    name = "rms-backend@zsc"
    url = "git@git.fuwu178.cn:zsc/research-platform-backend.git"
    branchSpec = "+:refs/heads/*"
    userNameStyle = EMAIL
    authMethod = uploadedKey {
        userName = "git"
        uploadedKey = "zsiot"
    }
})

object Frontend : GitVcsRoot({
    name = "rms-frontend@zsc"
    url = "git@git.fuwu178.cn:zsc/research-platform-frontend.git"
    branchSpec = "+:refs/heads/*"
    userNameStyle = EMAIL
    authMethod = uploadedKey {
        userName = "git"
        uploadedKey = "zsiot"
    }
})