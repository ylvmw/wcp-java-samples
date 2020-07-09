package com.vmware.solution.wcp;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Response;
import com.vmware.solution.wcp.model.TanzuKubernetesCluster;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Pair;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Secret;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

public class CreateTanzuKubernetesCluster {

	public static void main(String[] args) throws Exception {
		String supervisorKubeconfig = System.getProperty("user.home") +  "/.kube/sc.config"; // The supervisor cluster kubeconfig file path
		String namespace = "demo"; // The workload namespace to create the tkc
		String tkcYamlPath = "src/main/resources/tkc.yaml"; // The TanzuKubernetesCluster spec in YAML format
		
        org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
        TanzuKubernetesCluster tkc = yaml.loadAs(new FileReader(tkcYamlPath), TanzuKubernetesCluster.class);
		ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(supervisorKubeconfig))).build();
		String tkcName = tkc.getMetadata().getName();
		createTkc(client, tkcYamlPath, namespace);
		waitTkcReady(client, tkcName, namespace);
		String tkcKubeconfig = getTkcKubeConfig(client, tkcName, namespace);
		System.out.println(tkcKubeconfig);
	}
	
    private static void createTkc(ApiClient apiClient, String tkcYamlPath, String namespace) throws ApiException, IOException {
        String subPath = "/apis/run.tanzu.vmware.com/v1alpha1/namespaces/"+ namespace + "/tanzukubernetesclusters";
        byte[] body = Files.readAllBytes(Paths.get(tkcYamlPath));
        String resp = sendRequest(apiClient, subPath, "POST", "application/yaml", body);
        System.out.println("Create Tanzu Kubernetes Cluster: response=" + resp);
    }
    
	private static void waitTkcReady(final ApiClient apiClient, final String tkcName, String namespace) {
		final String subPath = "/apis/run.tanzu.vmware.com/v1alpha1/namespaces/"+ namespace + "/tanzukubernetesclusters/" + tkcName;
		System.out.println("Wait for cluster ready at most 1 hour...");
		for (int i = 0; i < 720; i++) {
			try {
				String resp = sendRequest(apiClient, subPath, "GET", null, null);
				Gson gson = new Gson();
				TanzuKubernetesCluster mc = gson.fromJson(resp, TanzuKubernetesCluster.class);
				String phase = mc.getStatus().getPhase();
				if ("running".equals(phase)) {
					System.out.println("Cluster is ready");
					break;
				} else {
					System.out.println("Cluster is not ready: phase=" + phase);
				}
				Thread.sleep(5000);
			} catch (Exception e) {
				System.out.println("Exception when waiting for cluster ready, error: " + e.getMessage());
			}
		}
	}
    
    private static String getTkcKubeConfig(ApiClient apiClient, String tkcName, String namespace) throws ApiException {
        CoreV1Api api = new CoreV1Api(apiClient);
        V1Secret secret = api.readNamespacedSecret(tkcName + "-kubeconfig", namespace, null, null, null);
        return new String(secret.getData().get("value"));
    }
	
    private static String sendRequest(ApiClient apiClient, String subPath, String method, String contentType, Object body) throws ApiException, IOException {
        try {
            List<Pair> localVarQueryParams = new ArrayList<Pair>();
            List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
            Map<String, String> localVarHeaderParams = new HashMap<String, String>();
            Map<String, Object> localVarFormParams = new HashMap<String, Object>();

            localVarHeaderParams.put("Content-Type", contentType);
            localVarHeaderParams.put("Accept", "application/json, application/yaml, application/vnd.kubernetes.protobuf");

            String[] localVarAuthNames = new String[] { "BearerToken" };
            Call call = apiClient.buildCall(subPath, method, localVarQueryParams, localVarCollectionQueryParams, body, localVarHeaderParams, localVarFormParams, localVarAuthNames, null);
            Response resp = call.execute();
            String respBody = resp.body().string();
            if (resp.isSuccessful()) {
               return respBody;
            } else {
                throw new RuntimeException(respBody);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
