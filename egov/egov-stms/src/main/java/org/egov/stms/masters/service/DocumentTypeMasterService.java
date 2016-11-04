/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.stms.masters.service;

import java.util.List;

import org.egov.stms.masters.entity.DocumentTypeMaster;
import org.egov.stms.masters.entity.SewerageApplicationType;
import org.egov.stms.masters.repository.DocumentTypeMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DocumentTypeMasterService {
  
    @Autowired
    private  DocumentTypeMasterRepository documentTypeMasterRepository;

   
    public DocumentTypeMaster findOne(final Long id) {
        return documentTypeMasterRepository.findOne(id);
    }

    @Transactional
    public DocumentTypeMaster createDocumentName(final DocumentTypeMaster DocumentTypeMaster) {
        DocumentTypeMaster.setActive(true);
        return documentTypeMasterRepository.save(DocumentTypeMaster);
    }

    @Transactional
    public void updateDocumentName(final DocumentTypeMaster DocumentTypeMaster) {
        documentTypeMasterRepository.save(DocumentTypeMaster);
    }

    public List<DocumentTypeMaster> findAll() {
        return documentTypeMasterRepository.findAll(new Sort(Sort.Direction.DESC, "description"));
    }

    public DocumentTypeMaster findByDescription(final String description) {
        return documentTypeMasterRepository.findByDescription(description);
    }

    public DocumentTypeMaster load(final Long id) {
        return documentTypeMasterRepository.getOne(id);
    }

    public Page<DocumentTypeMaster> getListOfDocumentTypeMaster(final Integer pageNumber, final Integer pageSize) {
        final Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "description");
        return documentTypeMasterRepository.findAll(pageable);
    }

    public List<DocumentTypeMaster> findByApplicationType(final SewerageApplicationType applicationType) {
        return documentTypeMasterRepository.findByApplicationType(applicationType);
    }

    public DocumentTypeMaster findByApplicationTypeAndDescription(final SewerageApplicationType applicationType,
            final String description) {
        return documentTypeMasterRepository.findByApplicationTypeAndDescription(applicationType, description);
    }

    public List<DocumentTypeMaster> getAllActiveDocumentTypeMasterByApplicationType(final SewerageApplicationType applicationType) {
        return documentTypeMasterRepository.findAllActiveDocumentTypeMasterByApplicationType(applicationType);
    }

}
