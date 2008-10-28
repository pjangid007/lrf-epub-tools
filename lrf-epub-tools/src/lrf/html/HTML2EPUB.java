package lrf.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import lrf.epub.EPUBMetaData;

public class HTML2EPUB extends EPUBMetaData {
	String id,creator,title;
	File htmlFile;
	HtmlOptimizer ho;
	@Override
	public String getCreator() {
		return creator;
	}

	@Override
	public String getIdentifier() {
		return id;
	}

	@Override
	public String getLanguage() {
		return "es";
	}

	@Override
	public String getPublisher() {
		return "LRFTools";
	}

	@Override
	public String getRights() {
		return "free";
	}

	@Override
	public Vector<String> getSubject() {
		return null;
	}

	@Override
	public String getTitle() {
		return title;
	}
	
	public HTML2EPUB(String fname,String id, String tit, String aut) throws Exception{
		title=tit;
		creator=aut;
		this.id=id;
		htmlFile=new File(fname);
		if(!htmlFile.exists())
			throw new RuntimeException(htmlFile.getAbsolutePath()+" no existe");
		process();
	}

	private void process() throws FileNotFoundException,
			IOException, Exception {
		
		File dirOfHTMLFile=htmlFile.getParentFile();
		String nameNoExt=htmlFile.getName();
		nameNoExt=nameNoExt.substring(0,nameNoExt.lastIndexOf('.'));
		init(dirOfHTMLFile.getAbsolutePath()+File.separator+nameNoExt+".epub");
		//Convertimos a xhtml
		String contenido=htmlToXhtml(new FileInputStream(htmlFile));
		File xhtmlFile=new File(dirOfHTMLFile,nameNoExt+".xhtml");
		FileOutputStream fos=new FileOutputStream(xhtmlFile);
		fos.write(contenido.getBytes());
		fos.close();
		HtmlOptimizer opt=new HtmlOptimizer(xhtmlFile,dirOfHTMLFile);
		opt.setPaginateKB(150);
		int pages=opt.optimize(true);
		buildCSS(nameNoExt+".css", opt.getStyles());
		//Generamos epub
		//xhtml
		for(int i=0;i<pages;i++){
			String whole=nameNoExt+"-"+(1+i)+".html";
			File f=new File(dirOfHTMLFile,whole);
			processFile(f, whole);
		}
		close();
	}
	
	public static void main(String args[]){
		try {
			HTML2EPUB uno=new HTML2EPUB(
				"d:\\tmp\\Vazquez Figueroa, Alberto-Maremagnum.htm",
				"djkh",
				"Maremagnum",
				"Vazquez Figueroa, Alberto");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}