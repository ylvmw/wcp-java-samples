package com.vmware.solution.wcp.model;

public class TanzuKubernetesCluster {
    
    public static class Metadata {
        
        private String name;

        private String namespace;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

    }
    
    public static class Spec {
        
        private Object topology;
        
        private Object distribution;
        
        private Object settings;

        public Object getTopology() {
            return topology;
        }

        public Object getDistribution() {
            return distribution;
        }

        public Object getSettings() {
            return settings;
        }

        public void setTopology(Object topology) {
            this.topology = topology;
        }

        public void setDistribution(Object distribution) {
            this.distribution = distribution;
        }

        public void setSettings(Object settings) {
            this.settings = settings;
        }

    }
    
    public static class Status {
        
        private String phase;

        public String getPhase() {
            return phase;
        }

        public void setPhase(String phase) {
            this.phase = phase;
        }

    }
    
    private String apiVersion;

    private String kind;
    
    private Metadata metadata;

    private Spec spec;

    private Status status;
    
    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Spec getSpec() {
        return spec;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
