package read;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadData {
	private List<String> attribute=new LinkedList<String>();  //属性链表
	private HashMap<String,String[]> att_val=new HashMap<String,String[]>(); //各属性的取值
	private List<ArrayList<String>> data=new LinkedList<ArrayList<String>>();  //训练集元祖数据
	
	public List<String> getArrtibute(){
		return this.attribute;
	}
	public HashMap<String,String[]> getAttVal(){
		return this.att_val;
	}
	public List<ArrayList<String>> getData(){
		return this.data;
	}
	
	
	public void  readArff(String path) throws IOException{   //数 入为数据文件路径
		boolean isData=false;    //判断下面的行是否为元数据

		Pattern pattern=Pattern.compile("@attribute\\s?(\\S+)\\s?[{]([\\s\\S]+)[}]");//匹配属性

		FileReader file=new FileReader(path); 
		BufferedReader br=new BufferedReader(file);
		
		String line=br.readLine();
		while(line!=null){
			if(line.contains("@data")){
				isData=true;
				line=br.readLine();
				continue;
			}
			if(isData){
				String[] row=line.split(",");
				ArrayList<String> row_array=new ArrayList<String>();
				for(int i=0;i<row.length;i++){
					row_array.add(row[i]);
				}
				data.add(row_array);
			}else{
				Matcher matcher=pattern.matcher(line);
				if(matcher.find()){
					String[] value=matcher.group(2).split(",");
					attribute.add(matcher.group(1));
					att_val.put(matcher.group(1),value);
				}
			}
			line=br.readLine();
			
		}
		br.close();		
	}
}
