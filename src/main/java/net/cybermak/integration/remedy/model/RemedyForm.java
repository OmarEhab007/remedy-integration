package net.cybermak.integration.remedy.model;

import java.util.List;
import java.util.Map;

/**
 * BMC Remedy form metadata
 * TDD: Minimal implementation to satisfy RemedyFormHandlerTest
 */
public class RemedyForm {
    
    private final String formName;
    private final List<String> requiredFields;
    private final Map<String, String> fieldTypes;
    
    public RemedyForm(String formName, List<String> requiredFields, Map<String, String> fieldTypes) {
        this.formName = formName;
        this.requiredFields = requiredFields;
        this.fieldTypes = fieldTypes;
    }
    
    public String getFormName() {
        return formName;
    }
    
    public List<String> getRequiredFields() {
        return requiredFields;
    }
    
    public Map<String, String> getFieldTypes() {
        return fieldTypes;
    }
    
    public boolean isRequiredField(String fieldName) {
        return requiredFields.contains(fieldName);
    }
    
    public String getFieldType(String fieldName) {
        return fieldTypes.get(fieldName);
    }
}