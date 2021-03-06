/*
 * Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 * 
 *  http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.services.ec2.model;

import java.io.Serializable;

/**
 * <p>
 * Contains the output of CreateInternetGateway.
 * </p>
 */
public class CreateInternetGatewayResult implements Serializable {

    /**
     * Information about the Internet gateway.
     */
    private InternetGateway internetGateway;

    /**
     * Information about the Internet gateway.
     *
     * @return Information about the Internet gateway.
     */
    public InternetGateway getInternetGateway() {
        return internetGateway;
    }
    
    /**
     * Information about the Internet gateway.
     *
     * @param internetGateway Information about the Internet gateway.
     */
    public void setInternetGateway(InternetGateway internetGateway) {
        this.internetGateway = internetGateway;
    }
    
    /**
     * Information about the Internet gateway.
     * <p>
     * Returns a reference to this object so that method calls can be chained together.
     *
     * @param internetGateway Information about the Internet gateway.
     *
     * @return A reference to this updated object so that method calls can be chained
     *         together.
     */
    public CreateInternetGatewayResult withInternetGateway(InternetGateway internetGateway) {
        this.internetGateway = internetGateway;
        return this;
    }

    /**
     * Returns a string representation of this object; useful for testing and
     * debugging.
     *
     * @return A string representation of this object.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getInternetGateway() != null) sb.append("InternetGateway: " + getInternetGateway() );
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;
        
        hashCode = prime * hashCode + ((getInternetGateway() == null) ? 0 : getInternetGateway().hashCode()); 
        return hashCode;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        if (obj instanceof CreateInternetGatewayResult == false) return false;
        CreateInternetGatewayResult other = (CreateInternetGatewayResult)obj;
        
        if (other.getInternetGateway() == null ^ this.getInternetGateway() == null) return false;
        if (other.getInternetGateway() != null && other.getInternetGateway().equals(this.getInternetGateway()) == false) return false; 
        return true;
    }
    
}
    