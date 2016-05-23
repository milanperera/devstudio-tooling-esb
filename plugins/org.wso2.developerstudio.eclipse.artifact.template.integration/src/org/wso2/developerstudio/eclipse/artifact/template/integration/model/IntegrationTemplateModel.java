/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.developerstudio.eclipse.artifact.template.integration.model;

import java.io.File;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.wso2.developerstudio.eclipse.artifact.template.model.TemplateModel;
import org.wso2.developerstudio.eclipse.platform.core.exception.ObserverFailedException;

public class IntegrationTemplateModel extends TemplateModel {

    public void setLocation(File location) {
        super.setLocation(location);
        File absolutionPath = getLocation();
        if (getTemplateSaveLocation() == null && absolutionPath != null) {
            IContainer newTemplateSaveLocation =
                                               getContainer(absolutionPath,
                                                            "org.wso2.developerstudio.eclipse.integration.project.nature");
            setTemplateSaveLocation(newTemplateSaveLocation);
        }
    }

    public Object getModelPropertyValue(String key) {
        Object modelPropertyValue = super.getModelPropertyValue(key);
        // overwrite save location from super even if it is set earlier
        if (key.equals("save.file")) {
            IContainer container = getTemplateSaveLocation();
            if (container != null && container instanceof IFolder) {
                IFolder templatesFolder = container.getProject().getFolder("src").getFolder("main")
                                                   .getFolder("integration-config")
                                                   .getFolder("integration-templates");
                modelPropertyValue = templatesFolder;
            } else {
                modelPropertyValue = container;
            }
        }
        return modelPropertyValue;
    }

    public boolean setModelPropertyValue(String key, Object data) throws ObserverFailedException {
        boolean returnResult = super.setModelPropertyValue(key, data);
        if (key.equals("save.file")) {
            IContainer container = (IContainer) data;
            if (container != null && container instanceof IFolder) {
                IFolder templateFolder = container.getProject().getFolder("src").getFolder("main")
                                                  .getFolder("integration-config")
                                                  .getFolder("integration-templates");
                setTemplateSaveLocation(templateFolder);
            } else {
                setTemplateSaveLocation(container);
            }
        }
        return returnResult;
    }
}
