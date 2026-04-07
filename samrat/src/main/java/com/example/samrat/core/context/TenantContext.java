package com.example.samrat.core.context;

public class TenantContext {
    private static final ThreadLocal<Long> CURRENT_HOSPITAL_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> CURRENT_BRANCH_ID = new ThreadLocal<>();

    public static void setHospitalId(Long hospitalId) {
        CURRENT_HOSPITAL_ID.set(hospitalId);
    }

    public static Long getHospitalId() {
        return CURRENT_HOSPITAL_ID.get();
    }

    public static void setBranchId(Long branchId) {
        CURRENT_BRANCH_ID.set(branchId);
    }

    public static Long getBranchId() {
        return CURRENT_BRANCH_ID.get();
    }

    public static void clear() {
        CURRENT_HOSPITAL_ID.remove();
        CURRENT_BRANCH_ID.remove();
    }
}
