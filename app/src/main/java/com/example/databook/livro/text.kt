package com.example.databook.livro
/*
import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.widget.Toast
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import kotlin.jvm.Throws

fun setPdfFunction() {
    Log.i("PDF", "entrou na função")
    Dexter.withActivity(this)
        .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                val file = File(pdfPath, "myPDF.pdf")
                createPdfFile(pdfPath)
                // createPdfFile(Common.getAppPath(this@LivroSelecionadoActivity)+"teste_pdf.pdf")
                Log.i("PDF", "onPermissionGranted")


            }

            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest?,
                token: PermissionToken?
            ) {
                Log.i("PDF", "onPermissionRationaleShouldBeShowno")
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                Log.i("PDF", "onPermissionDenied")
            }
        })
        .check()


}

private fun createPdfFile(path: String) {
    if (File(path).exists())
        File(path).delete()
    try {
        val document = Document()
        //save
        PdfWriter.getInstance(document, FileOutputStream(path))
        //Open to write
        document.open()

        //Setting
        document.pageSize = PageSize.A4
        document.addCreationDate()
        document.addAuthor("DataBook")
        document.addCreator("DataBook App")

        //Font setting
        val colorAccent = BaseColor(0, 153, 204, 255)
        val headingFontSize = 20.0f
        val valueFontSize = 26.0f

        //custom font
        val fontName =
            BaseFont.createFont("res/font/montserrat_regular.ttf", "UTF-8", BaseFont.EMBEDDED)

        //Add Title to document
        val titleStyle = Font(fontName, 36.0f, Font.NORMAL, BaseColor.BLACK)
        addNewItem(document, "Order Details", Element.ALIGN_CENTER, titleStyle)

        val headingStyle = Font(fontName, headingFontSize, Font.NORMAL, colorAccent)
        addNewItem(document, "Order No:", Element.ALIGN_LEFT, headingStyle)

        val valueStyle = Font(fontName, valueFontSize, Font.NORMAL, BaseColor.BLACK)
        addNewItem(document, "#1233123:", Element.ALIGN_LEFT, valueStyle)

        addLineSeparator(document)

        addNewItem(document, "Order Date:", Element.ALIGN_LEFT, headingStyle)
        addNewItem(document, "03/08/2019", Element.ALIGN_LEFT, valueStyle)

        addLineSeparator(document)

        addNewItem(document, "Account Name:", Element.ALIGN_LEFT, headingStyle)
        addNewItem(document, "Eddy Lee", Element.ALIGN_LEFT, valueStyle)

        addLineSeparator(document)

        //product Details
        addLineSpace(document)
        addNewItem(document, "Product Details", Element.ALIGN_CENTER, titleStyle)

        addLineSeparator(document)

        //Item1
        addNewItemWithLeftAndRight(document, "Pizza 25", "0.0%", titleStyle, valueStyle)
        addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleStyle, valueStyle)

        addLineSeparator(document)

        //Item2
        addNewItemWithLeftAndRight(document, "Pizza 26", "0.0%", titleStyle, valueStyle)
        addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleStyle, valueStyle)

        addLineSeparator(document)

        //Total
        addLineSpace(document)
        addLineSpace(document)

        addNewItemWithLeftAndRight(document, "Total", "24000.0", titleStyle, valueStyle)


        //Close
        document.close()

        Toast.makeText(this@LivroSelecionadoActivity, "Success", Toast.LENGTH_SHORT).show()

        printPDF()
    } catch (e: Exception) {
        Log.e("PDF", "print" + e.message)
    }
}

private fun printPDF() {
    val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
    try {
        Log.e("pathPDF", pdfPath)
        val prinAdapter = PdfDocumentAdapter(this@LivroSelecionadoActivity, pdfPath)
        printManager.print("Document", prinAdapter, PrintAttributes.Builder().build())
    } catch (e: Exception) {
        Log.e("PDF", "printPDF" + e.message)
    }
}

@Throws(DocumentException::class)
private fun addNewItemWithLeftAndRight(
    document: Document,
    textLeft: String,
    textRight: String,
    leftStyle: Font,
    rightStyle: Font
) {
    val chunkTextLeft = Chunk(textLeft, leftStyle)
    val chunkTextRight = Chunk(textLeft, rightStyle)
    val p = Paragraph(chunkTextLeft)
    p.add(Chunk(VerticalPositionMark()))
    p.add(chunkTextRight)
    document.add(p)

}

@Throws(DocumentException::class)
private fun addLineSeparator(document: Document) {
    val lineSeparator = LineSeparator()
    lineSeparator.lineColor = BaseColor(0, 0, 0, 68)
    addLineSpace(document)
    document.add(Chunk(lineSeparator))
    addLineSpace(document)

}

@Throws(DocumentException::class)
private fun addLineSpace(document: Document) {
    document.add(Paragraph(""))

}

@Throws(DocumentException::class)
private fun addNewItem(document: Document, text: String, align: Int, style: Font) {
    val chunk = Chunk(text, style)
    val p = Paragraph(chunk)
    p.alignment = align
    document.add(p)
}
*/