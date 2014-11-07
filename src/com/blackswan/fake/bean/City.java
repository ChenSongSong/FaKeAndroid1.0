package com.blackswan.fake.bean;

/**
 * @author CSSPeter
 *
 * 2014年9月3日
 */
public class City
{
	private String name;
	private String province;
	private String provinceSort;
	private String nameSort;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getProvince()
	{
		return province;
	}
	public void setProvince(String province)
	{
		this.province = province;
	}
	public String getNameSort()
	{
		return nameSort;
	}
	public void setNameSort(String nameSort)
	{
		this.nameSort = nameSort;
	}
	public String getProvinceSort()
	{
		return provinceSort;
	}
	public void setProvinceSort(String provinceSort)
	{
		this.provinceSort = provinceSort;
	}
	
	public void setCity(String name,String province,String nameSort,String provinceSort)
	{
		this.name = name;
		this.province = province;
		this.nameSort = nameSort;
		this.provinceSort = provinceSort;
	}
	
	//添加热门城市
	public void setHotCity(String name) {
		this.name = name;
		this.province = "热门";
		this.nameSort = "热门";
		this.provinceSort = "热门";
	}
	
	//添加最近定位城市
	public void setLastCity(String name) {
		this.name = name;
		this.province = "最近";
		this.nameSort = "最近";
		this.provinceSort = "最近";
	}
	

}
