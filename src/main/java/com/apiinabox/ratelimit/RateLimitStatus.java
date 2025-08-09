package com.apiinabox.ratelimit;

/**
 * Status information for rate limiters
 */
public class RateLimitStatus {
    private final int availablePermissions;
    private final int totalPermissions;
    private final String key;
    
    public RateLimitStatus(int availablePermissions, int totalPermissions, String key) {
        this.availablePermissions = availablePermissions;
        this.totalPermissions = totalPermissions;
        this.key = key;
    }
    
    public int getAvailablePermissions() {
        return availablePermissions;
    }
    
    public int getTotalPermissions() {
        return totalPermissions;
    }
    
    public String getKey() {
        return key;
    }
    
    public int getUsedPermissions() {
        return totalPermissions - availablePermissions;
    }
    
    @Override
    public String toString() {
        return String.format("%s: %d/%d used", key, getUsedPermissions(), totalPermissions);
    }
}