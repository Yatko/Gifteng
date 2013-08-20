<?php

/**
 * Custom form validation class, that adds an extra function to check if the
 * form validation at that moment (when invoked) contains errors.
 * Useful for callback validators when they communicates with the WS, on any previous
 * validation fail the callback can be skipped with this simple check (hasErrors).
 *
 * @author gyuszi
 */
class Venefica_Form_validation extends CI_Form_validation {
    
    /**
     * Check if the validation generated any error.
     * 
     * @return boolean
     */
    public function hasErrors() {
        $total_errors = count($this->_error_array);
        if ( $total_errors > 0 ) {
            return TRUE;
        }
        return FALSE;
    }
    
    /**
     * Sets a general form error. This error is not coupled with any field.
     * 
     * @param string $error
     */
    public function setError($error) {
        $this->_error_array['xxx'] = $error;
    }
    
    /**
     * Sets a form field value.
     *  
     * @param string $field
     * @param string $value
     */
    public function setFieldValue($field, $value) {
        $this->_field_data[$field]['postdata'] = $value;
    }
}

?>
