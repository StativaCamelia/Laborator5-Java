package com.company;

public class HtmlReport {
    private Catalog catalog = new Catalog();
    private StringBuilder htmlReport = new StringBuilder();

    HtmlReport(Catalog catalog){
        this.catalog = catalog;
    }

    public void createHtmlReport(){
        htmlReport.append( "<!doctype html>\n" );
        htmlReport.append( "<html lang='en'>\n" );

        htmlReport.append( "<head>\n" );
        htmlReport.append( "<meta charset='utf-8'>\n" );
        htmlReport.append( "<title>Report</title>\n" );
        htmlReport.append( "</head>\n\n" );

        htmlReport.append( "<body>\n" );
        htmlReport.append( "<h1>Reports</h1>\n" );
        // Make a list in HTML
        htmlReport.append( "<ul>\n" );
        // Loop the list of reports passed as argument.
        for ( Document report : catalog.getDocuments() ) {
            htmlReport.append( "<li>" + report + "</li>\n" );
        }
        htmlReport.append( "</ul>\n" );
        htmlReport.append( "</body>\n\n" );

        htmlReport.append( "</html>" );
    }

    public StringBuilder getHtmlReport() {
        return htmlReport;
    }

    public void setHtmlReport(StringBuilder htmlReport) {
        this.htmlReport = htmlReport;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }


}
