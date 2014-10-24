<%@ page import="com.actuate.aces.idapi.FileLister" %>
<%@ page import="com.actuate.schemas.File" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2014 Actuate Corporation
  --%>

<html>
<head><title>File Listing</title></head>
<body>

<%
    String baseFolder = request.getParameter("folder");
    if (baseFolder == null || baseFolder.equals(""))
        baseFolder = "/";
%>
<h1>Files in Folder: <%=baseFolder%>
</h1>
<table>
    <tr>
        <th>Name</th>
        <th>Version</th>
        <th>Size</th>
    </tr>
    <%

        FileLister fileLister = new FileLister("http://localhost:8000", "Administrator", "", "Default Volume");
        ArrayList<File> files = fileLister.getFileList(baseFolder);

        for (File file : files) {
            String name = file.getName();
            String version = file.getVersionName() != null ? file.getVersionName() : "";
            Long size = file.getSize();
            String type = file.getFileType();
            if (type.equalsIgnoreCase("DIRECTORY")) {
                if (!baseFolder.equals("/"))
                    baseFolder = baseFolder + "/";

                name = "<a href='filelisting.jsp?folder=" + baseFolder + name + "'>" + name + "</a>";
            }
    %>
    <tr>
        <td><%=name%>
        </td>
        <td><%=version%>
        </td>
        <td><%=size%>
        </td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>