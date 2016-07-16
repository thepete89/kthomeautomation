package de.kawumtech.core.navigation.model;

import java.util.ArrayList;
import java.util.List;

public class NavigationElement
{
	private String elementTitle;
	
	private String elementTag;
	
	private String elementLink;
	
	private Boolean active = Boolean.FALSE;
	
	private Boolean divider = Boolean.FALSE;
	
	private List<NavigationElement> children = new ArrayList<NavigationElement>();
	
	public NavigationElement(String elementTitle, String elementTag, String elementLink)
	{
		this.elementLink = elementLink;
		this.elementTag = elementTag;
		this.elementTitle = elementTitle;
	}
	
	public String getElementTitle()
	{
		return elementTitle;
	}

	public void setElementTitle(String elementTitle)
	{
		this.elementTitle = elementTitle;
	}

	public String getElementTag()
	{
		return elementTag;
	}

	public void setElementTag(String elementTag)
	{
		this.elementTag = elementTag;
	}

	public String getElementLink()
	{
		return elementLink;
	}

	public void setElementLink(String elementLink)
	{
		this.elementLink = elementLink;
	}

	public Boolean getActive()
	{
		return active;
	}

	public void setActive(Boolean active)
	{
		this.active = active;
	}

	public List<NavigationElement> getChildren()
	{
		return children;
	}

	public void setChildren(List<NavigationElement> children)
	{
		this.children = children;
	}

	public Boolean getDivider()
	{
		return divider;
	}

	public void setDivider(Boolean divider)
	{
		this.divider = divider;
	}
	
	
}
