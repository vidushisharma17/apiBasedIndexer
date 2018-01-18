package com.vidushisharma.searchEngine

import java.io.File;
import java.io.FileReader;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

class Indexer(name:String , location:String, fees:String , speciality:String){
  //------------------------------------------------------//
  private val SourceFilePath="/Users/vidushisharma/IdeaProjects/ApiSearchEngine/src/main/resources" //Location of source files which we want to index
  private val indexFilePath="/Users/vidushisharma/IdeaProjects/ApiSearchEngine/src/main/resources/IndexedFiles"   //Location where we want to create the Index
  private var writer:IndexWriter =null
  private var indexDirectory: File = null

  //------------------------------------------------------//

  def Indexer(){
    try {
      createIndexWriter();
      //checkFileValidity();
      closeIndexWriter();
      println("Total Documents Indexed : " + TotalDocumentsIndexed());
    } catch {
      case e:Throwable=>e.printStackTrace

    }
  }
  //------------------------------------------------------//

  private def createIndexWriter(): Unit ={
    try{
      println("Inside createIndexWriter")
      indexDirectory=new File(indexFilePath)
      if(!indexDirectory.exists()){
        indexDirectory.mkdir()
        println("Directory Made")

      }
      val dir:FSDirectory=FSDirectory.open(indexDirectory)
      val analyzer:StandardAnalyzer=new StandardAnalyzer(Version.LUCENE_34)
      val config:IndexWriterConfig=new IndexWriterConfig(Version.LUCENE_34,analyzer)
      writer=new IndexWriter(dir,config)
      indexTextFiles()
    }catch {
      case ex:Throwable=>println("Sorry cannot get the index writer")
    }
  }
  //------------------------------------------------------//


 /* private def checkFileValidity(): Unit ={
    var filesToIndex:Array[File]=new Array[File](100) // 100 Files Max
    filesToIndex=new File(SourceFilePath).listFiles
    for(file<-filesToIndex){
      try{
        if(!(file.isDirectory) && !(file.isHidden) && file.exists() && file.canRead && file.length()>0.0
          && file.isFile && file.getName.endsWith(".txt")){
          println("Indexing File with "+file.getAbsolutePath)
          indexTextFiles(file)
          println("Indexed File "+file.getAbsolutePath)

        }


      }catch{
        case e:Exception=>
          println("Cant index "+file.getAbsolutePath)
      }
    }

  }*/

  val nameValue:String =name
  val locationValue:String =location
  val feesValue:String =fees
  val specialityValue:String =speciality

  //----------------------------------------------------------------//
  private def indexTextFiles(): Unit ={
    val doc:Document=new Document

    //doc.add(new Field("content",new FileReader(file)))
    doc.add(new Field("Name", nameValue, Field.Store.YES, Field.Index.ANALYZED))
    doc.add(new Field("Location", locationValue, Field.Store.YES, Field.Index.ANALYZED))
    doc.add(new Field("Fees", feesValue, Field.Store.YES, Field.Index.ANALYZED))
    doc.add(new Field("Speciality", specialityValue, Field.Store.YES, Field.Index.ANALYZED))
   // doc.add(new Field("filename", file.getName, Field.Store.YES, Field.Index.ANALYZED))
   // doc.add(new Field("fullpath",file.getAbsolutePath,Field.Store.YES,Field.Index.ANALYZED))
    if(doc!=null)
    {
      writer.addDocument(doc)
    }
  }

  //----------------------------------------------------------------//
  private def TotalDocumentsIndexed(): Int ={
    try{
      val reader:IndexReader=IndexReader.open(FSDirectory.open(indexDirectory))
      return reader.maxDoc()

    }
    catch{
      case ex:Throwable=>println("NO INDEX FOUND")
    }
    return 0
  }
  //----------------------------------------------------------------//
  private def closeIndexWriter(): Unit ={
    try{
      writer.close()
    }
    catch{
      case e:Exception=>println("Index cant be closed")
    }
  }
  //-----------------------------------------------------------------//

}