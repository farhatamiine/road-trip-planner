package com.amine.roadtripplanner.Exception;

public class ResourceNotFoundException extends RuntimeException {
    private final String resourceType;
    private final String resourceId;

    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(String.format("%s not found with id: %s", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceType = null;
        this.resourceId = null;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}