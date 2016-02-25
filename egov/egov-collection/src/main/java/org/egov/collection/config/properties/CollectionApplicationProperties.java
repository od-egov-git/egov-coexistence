/* eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.collection.config.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(name = "collectionApplicationProperties", 
value = { "classpath:config/payment-gateway.properties",
        "classpath:config/egov-erp-${user.name}.properties",
        "classpath:config/application-config-${client.id}.properties",
        "classpath:config/egov-erp-override.properties" }, ignoreResourceNotFound = true)
public class CollectionApplicationProperties {

    @Autowired
    private Environment environment;

    public String axisTransactionMessage() {
        return environment.getProperty("AXIS.transactionmessage", String.class);
    }

    public Integer axisVersion() {
        return environment.getProperty("axis.version", Integer.class);
    }

    public String axisCommand() {
        return environment.getProperty("axis.command", String.class);
    }

    public String axisCommandQuery() {
        return environment.getProperty("axis.command.query", String.class);
    }

    public String axisAccessCode() {
        return environment.getProperty("axis.access.code", String.class);
    }

    public String axisMerchant() {
        return environment.getProperty("axis.merchant", String.class);
    }

    public String axisLocale() {
        return environment.getProperty("axis.locale", String.class);
    }

    public String axisOperator() {
        return environment.getProperty("axis.operator.id", String.class);
    }

    public String axisPassword() {
        return environment.getProperty("axis.password", String.class);
    }

    public String axisReconcileUrl() {
        return environment.getProperty("axis.reconcile.url", String.class);
    }

    public String axisSecureSecret() {
        return environment.getProperty("axis.secure.secret", String.class);
    }
}
