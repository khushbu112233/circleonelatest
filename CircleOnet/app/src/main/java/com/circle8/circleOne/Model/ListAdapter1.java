package com.circle8.circleOne.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.circle8.circleOne.R;

import java.util.ArrayList;

public class ListAdapter1 extends ArrayAdapter<ListCell>
{

	LayoutInflater inflater;

	public ListAdapter1(Context context, ArrayList<ListCell> items)
	{
		super(context, 0, items);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		ListCell cell = getItem(position);
		
		//If the cell is a section header we inflate the header layout 
		/*if(cell.isSectionHeader())
		{
			v = inflater.inflate(R.layout.section_header, null);
			v.setClickable(false);
			
			TextView header = (TextView) v.findViewById(R.id.section_header);
			header.setText(cell.getName());
		}*/

		if(cell.isMonthHeader())
		{
			v = inflater.inflate(R.layout.section_header, null);
			v.setClickable(false);

			TextView header = (TextView) v.findViewById(R.id.section_header);
			header.setText(cell.getName());
		}
		else
		{
			v = inflater.inflate(R.layout.list_item2, null);
			TextView name = (TextView) v.findViewById(R.id.name);
			TextView category = (TextView) v.findViewById(R.id.category);

			TextView tvDate = (TextView)v.findViewById(R.id.tvDate);
			TextView tvContent = (TextView)v.findViewById(R.id.tvContent);
			TextView tvRupees = (TextView)v.findViewById(R.id.tvRupees);

			tvDate.setText(cell.getDate());
			tvContent.setText(cell.getContent());
			tvRupees.setText(cell.getRupee());
			
//			name.setText(cell.getName());
//			category.setText(cell.getCategory());
		}
		return v;
	}
}
