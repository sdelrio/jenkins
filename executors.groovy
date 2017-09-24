import jenkins.model.*

def jenkinsMasterExecutors = System.getenv("JENKINS_MASTER_EXECUTORS") ?: "4"
jenkinsMasterExecutors = jenkinsMasterExecutors.substring(1, jenkinsMasterExecutors.length()-1)
Jenkins.instance.setNumExecutors(jenkinsMasterExecutors.toInteger())
print "Jenkins Master Executors to " + jenkinsMasterExecutors + "\n"

