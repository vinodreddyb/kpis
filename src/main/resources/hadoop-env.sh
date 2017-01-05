export HADOOP_MAPRED_HOME=$( ([[ ! '/opt/cloudera/parcels/CDH/lib/hadoop-mapreduce' =~ CDH_MR2_HOME ]] && echo /opt/cloudera/parcels/CDH/lib/hadoop-mapreduce ) || echo ${CDH_MR2_HOME:-/usr/lib/hadoop-mapreduce/}  )
# HADOOP_CLASSPATH={{HADOOP_CLASSPATH}}
# JAVA_LIBRARY_PATH={{JAVA_LIBRARY_PATH}}
export YARN_OPTS="-Xms1073741824 -Xmx1073741824 -Djava.net.preferIPv4Stack=true $YARN_OPTS"
export HADOOP_CLIENT_OPTS="-Djava.net.preferIPv4Stack=true $HADOOP_CLIENT_OPTS"
