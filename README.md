# wcp-java-samples
## How to run CreateTanzuKubernetesCluster
1. import the maven project to IDE (eclipse or IDEA)
2. create the workload namespace on vSphere 7
3. prepare the tanzu kubernetes cluster creation spec, could refer to the one in the project: "src/main/resources/tkc.yaml"
4. retrieve the supervisor cluster kubeconfig
   ```
   export KUBECONFIG=~/.kube/sc.config
   export SC_IP=<supervisor_cluster_ip>
   kubectl vsphere login --server=$SC_IP --insecure-skip-tls-verify=true -u <vsphere_username>
   ```
5. modify the first 3 variables in main method as you created
   ```
    String supervisorKubeconfig = System.getProperty("user.home") +  "/.kube/sc.config"; // The supervisor cluster kubeconfig file path
	String namespace = "demo"; // The workload namespace to create the tkc
	String tkcYamlPath = "src/main/resources/tkc.yaml"; // The TanzuKubernetesCluster spec in YAML format
    ```
6. Run the application 
