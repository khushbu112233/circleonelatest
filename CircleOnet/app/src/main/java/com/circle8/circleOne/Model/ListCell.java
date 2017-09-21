package com.circle8.circleOne.Model;

public class ListCell implements Comparable<ListCell>
{

	private String name;
	private String category;
	private boolean isSectionHeader;

	private String date ;
	private String content ;
	private String rupee ;
	private String month ;
	private boolean isMonthHeader ;
	
	public ListCell(String name, String category)
	{
		this.name = name;
		this.category = category;
		isSectionHeader = false;
	}

	public ListCell(String month, String date, String content, String rupee)
	{
		this.month = month ;
		this.date = date ;
		this.content = content ;
		this.rupee = rupee ;
		isMonthHeader = false ;
	}

	public String getDate()
	{
		return date ;
	}

	public String getContent()
	{
		return content ;
	}

	public String getRupee()
	{
		return rupee ;
	}

	public String getMonth()
	{
		return month ;
	}

	public String getName()
	{
		return name;
	}
	
	public String getCategory()
	{
		return category;
	}
	
	public void setToSectionHeader()
	{
		isSectionHeader = true;
	}

	public void setMonthHeader()
	{
		isMonthHeader = true ;
	}

	public boolean isSectionHeader()
	{
		return isSectionHeader;
	}

	public boolean isMonthHeader()
	{
		return isMonthHeader ;
	}
	
	/*@Override
	public int compareTo(ListCell other)
	{
		return this.category.compareTo(other.category);
	}*/

	@Override
	public int compareTo(ListCell other)
	{
		return this.month.compareTo(other.month);
	}
}
