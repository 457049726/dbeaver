/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2020 DBeaver Corp and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jkiss.dbeaver.ext.mssql.ui.tools.maintenance;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.ext.mssql.model.SQLServerObject;
import org.jkiss.dbeaver.ext.mssql.model.SQLServerTable;
import org.jkiss.dbeaver.model.DBPEvaluationContext;
import org.jkiss.dbeaver.model.struct.DBSObject;
import org.jkiss.dbeaver.ui.tools.IUserInterfaceTool;
import org.jkiss.utils.CommonUtils;

import java.util.Collection;
import java.util.List;

public class SQLServerToolRebuild implements IUserInterfaceTool {
    @Override
    public void execute(IWorkbenchWindow window, IWorkbenchPart activePart, Collection<DBSObject> objects)
            throws DBException {
        List<SQLServerTable> tables = CommonUtils.filterCollection(objects, SQLServerTable.class);
        if (!tables.isEmpty()) {
            SQLDialog dialog = new SQLDialog(activePart.getSite(), tables);
            dialog.open();
        }
    }

    static class SQLDialog extends TableToolDialog {
        public SQLDialog(IWorkbenchPartSite partSite, Collection<SQLServerTable> selectedTables) {
            super(partSite, "Rebuild index(s)", selectedTables);
        }

        @Override
        protected void generateObjectCommand(List<String> lines, SQLServerObject object) {
            lines.add("ALTER INDEX ALL ON " + ((SQLServerTable) object).getFullyQualifiedName(DBPEvaluationContext.DDL) + " REBUILD ");
        }

        @Override
        protected void createControls(Composite parent) {
            createObjectsSelector(parent);
        }
    }

}
