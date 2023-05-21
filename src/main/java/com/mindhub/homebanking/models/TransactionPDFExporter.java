package com.mindhub.homebanking.models;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.itextpdf.text.Image;
import com.mindhub.homebanking.services.ClientService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionPDFExporter {
    private Account account;

    private Client client;
    private List<Transaction>listTransactions;

    public TransactionPDFExporter(Client client, Account account, List<Transaction> listTransactions) {
        this.client = client;
        this.account = account;
        this.listTransactions = listTransactions;
    }

    private static final String LOGO_PATH = new File("C:/Users/Usuarios/Desktop/REPOSITORIOS/homebanking/src/main/resources/static/web/assets/logoMunecoSinFondo.png").getAbsolutePath();

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.BLUE);
        cell.setPadding(4);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(BaseColor.WHITE);

        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Transaction Type", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Balance", font));
        table.addCell(cell);
    }


    private void writeTableData(PdfPTable table) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for(Transaction transaction:listTransactions){
            table.addCell(transaction.getTransactionDate().format(formatter));
            table.addCell(transaction.getDescription());
            table.addCell(String.valueOf(transaction.getType()));
            table.addCell((String.format(String.valueOf(transaction.getAmount()), "$0,0.00")));
            table.addCell(String.valueOf(transaction.getBalanceTransaction()));
        }
    }

    public void usePDFExport(HttpServletResponse response) throws DocumentException, IOException {
        var doc= new Document(PageSize.A4);
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();

        InputStream is = getClass().getResourceAsStream("/static/web/assets/logoMunecoSinFondo.png");
        Image logo = Image.getInstance(IOUtils.toByteArray(is));
        logo.setAbsolutePosition(50, 770); // Reduce este valor para bajar el logo
        logo.scaleToFit(40, 40); // Ajusta el tamaño del logo
        doc.add(logo);

        Font clientFontTitu = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        clientFontTitu.setColor(BaseColor.BLACK);
        Paragraph clientFontTit = new Paragraph("MINDHUB BROTHERS BANK", clientFontTitu);
        clientFontTit.setAlignment(Paragraph.ALIGN_CENTER);
        doc.add(clientFontTit);

        Font clientFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        clientFont.setColor(BaseColor.BLACK);
        Paragraph clientInfo = new Paragraph("Client: " + client.getFirstName()  + " " + client.getLastName(), clientFont);
        clientInfo.setAlignment(Paragraph.ALIGN_RIGHT);
        doc.add(clientInfo);

        Font clientFontAccount = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        clientFontAccount.setColor(BaseColor.BLACK);
        Paragraph clientInfoAccount = new Paragraph(    "Account Number: " + account.getNumber(), clientFont);
        clientInfoAccount.setAlignment(Paragraph.ALIGN_RIGHT);
        doc.add(clientInfoAccount);


        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(BaseColor.BLACK);
        Paragraph p = new Paragraph("List of Transactions", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        doc.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {4.0f, 4.5f, 4.0f, 4.0f, 4.0f});
        table.setSpacingBefore(10);
        writeTableHeader(table);
        writeTableData(table);
        doc.add(table);
        doc.close();

    }


}
