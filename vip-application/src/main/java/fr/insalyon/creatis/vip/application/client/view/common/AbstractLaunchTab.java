/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.client.view.common;

import fr.insalyon.creatis.vip.application.client.view.launch.DocumentationSection;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.launch.InputsStackSection;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractLaunchTab extends Tab {

    protected SectionStack sectionStack;
    protected AbstractLaunchStackSection launchSection;
    protected InputsStackSection inputsSection;
    protected DocumentationSection documentationSection;
    protected String applicationName;
    
    public AbstractLaunchTab(String applicationName) {
        this.applicationName = applicationName;
        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_APPLICATION) + " "
                + applicationName);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        this.setID(ApplicationConstants.getLaunchTabID(applicationName));

        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();

        sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);

        vLayout.addMember(sectionStack);
        this.setPane(vLayout);
    }
    
    protected void addDocumentationSection(){
        documentationSection = new DocumentationSection(applicationName);
        sectionStack.addSection(documentationSection);
    }
    
    protected void addInputsSection() {
        inputsSection = new InputsStackSection(this.getID());
        sectionStack.addSection(inputsSection);
    }

    public void loadInputsList() {
        inputsSection.loadData();
    }

    public void loadInput(String name, String values) {
        launchSection.loadInput(name, values);
    }

    public DocumentationSection getDescriptionSection() {
        return documentationSection;
    }
}
