# bamoe9-demos

## Setup your linux box

### Install Java jdk

```
sudo dnf remove -y java*

sudo dnf install -y java-11-openjdk
```

### Install Quarkus CLI https://quarkus.io/get-started/

```
curl -Ls https://sh.jbang.dev | bash -s - trust add https://repo1.maven.org/maven2/io/quarkus/quarkus-cli/

curl -Ls https://sh.jbang.dev | bash -s - app install --fresh --force quarkus@quarkusio
```

### Install Maven

set version
```
MVN_VER_BASE=3
MVN_VER=${MVN_VER_BASE}.8.6
```

download maven package
```
cd ~/Downloads
wget https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MVN_VER}/apache-maven-${MVN_VER}-bin.tar.gz
tar xzvf apache-maven-${MVN_VER}-bin.tar.gz
sudo mv apache-maven-${MVN_VER} /opt/
```

add env vars in .bashrc
```
if [[ $(grep "/opt/apache-maven-"${MVN_VER}"/bin" ./.bashrc | wc -l) -gt 0 ]]; then echo "Maven path alredy set !"; else echo "export PATH=\$PATH:/opt/apache-maven-"${MVN_VER}"/bin" >> ~/.bashrc; fi
if [[ $(grep "M2_HOME=/home" ./.bashrc | wc -l) -gt 0 ]]; then echo "M2 path alredy set !"; else echo "export M2_HOME=/home/\$USER/.m2/repository" >> ~/.bashrc; fi
```

logout or export path in your shell
```
export PATH=$PATH:/opt/apache-maven-${MVN_VER}/bin
export M2_HOME=/home/$USER/.m2/repository
```

test maven version
```
mvn -v
```

test maven
```
cd ~
mkdir test-maven
cd test-maven
mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
tree
cd my-app
mvn package
java -cp target/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App
cd ../..
rm -fR ./test-maven/
```

list downloaded maven repos tree
```
tree -d ~/.m2
```


### Install IBAMOE Maven Repo

your source package in a folder..
```
BAMOE_MVN_REPO=/mnt/...../IBAMOE/v9/9.0.1/bamoe-9.0.1-maven-repository.zip
```

install bamoe maven repo
```
mvn install:install-file -DgroupId=com.ibm.bamoe -DartifactId=bamoe-bom -Dversion=9.0.1.Final -Dpackaging=jar -Dfile=${BAMOE_MVN_REPO}
```

list installed content
```
ls -al ~/.m2/repository/com/ibm/bamoe/
ls -al ~/.m2/repository/com/ibm/bamoe/bamoe-bom/
```

## Create new projects

```
_QMP_V=2.16.7.Final
_GRP_ID=marco.demos
_PRJ_NAME=my-quick-kogito
_PRJ_VER=1.0.0-SNAPSHOT
_PLATF_VER=${_QMP_V}

mvn io.quarkus:quarkus-maven-plugin:${_QMP_V}:create \
    -DprojectGroupId=${_GRP_ID} \
    -DprojectArtifactId=${_PRJ_NAME} \
    -DprojectVersion=${_PRJ_VER} \
    -DplatformVersion=${_PLATF_VER} \
    -Dextensions=kogito-quarkus,dmn,resteasy-reactive-jackson,quarkus-smallrye-openapi,quarkus-smallrye-health

cd ./${_PRJ_NAME}
quarkus build

```

### update pom.xml with bamoe v9 tags

add in 
```
<project ...>
  <properties>

    <!-- ADD -->
    <kogito.bom.group-id>com.ibm.bamoe</kogito.bom.group-id>
    <kogito.bom.artifact-id>bamoe-bom</kogito.bom.artifact-id>
    <kogito.bom.version>9.0.1.Final</kogito.bom.version>
```

susbstitute in
```
<project ...>
  <dependencyManagement>
    <dependencies>
      <dependency>

        <!-- REMOVE -->
        <groupId>${quarkus.platform.group-id}</groupId>
        <artifactId>quarkus-kogito-bom</artifactId>
        <version>${quarkus.platform.version}</version>
        
        <!-- ADD -->
        <groupId>${kogito.bom.group-id}</groupId>
        <artifactId>${kogito.bom.artifact-id}</artifactId>
        <version>${kogito.bom.version}</version>
```

(mandatory library)

add in
```
<project ...>
  <dependencies>

    <!-- ADD -->
    <dependency>
      <groupId>com.ibm.bamoe</groupId>
      <artifactId>bamoe-ilmt-compliance-quarkus-pamoe</artifactId>
    </dependency>
```
### rebuild the project after pom.xml update

```
quarkus build
```

after the build you will find a file with extension '.swidtag', eg:

```
ibm.com_IBM_Process_Automation_Manager_Open_Edition-9.0.1.swidtag
```
this file is the mandatory tag for license metric

### test the project

```
quarkus dev
```

## IBM BAMOE References

https://www.ibm.com/support/pages/node/6999323

Tracking the BAMOE License use in IBM License Metric Tool (ILMT)
https://www.ibm.com/docs/en/ibamoe/9.0.x?topic=olmti-tracking-bamoe-license-use-in-license-metric-tool
