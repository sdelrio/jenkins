import jenkins.model.*

def jenkinsMasterExecutors = System.getenv("JENKINS_MASTER_EXECUTORS") ?: "4"
Jenkins.instance.setNumExecutors(jenkinsMasterExecutors.toInteger())
print "Jenkins Master Executors to " + jenkinsMasterExecutors + "\n"

