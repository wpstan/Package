package cn.tsplaycool.notes.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * 为了使自定义adapter中的viewHolder简洁，减少大量重复viewHoder的代码
 * @author Tans
 */
public class ViewHolder {

	public static <T extends View> T get(View convertView, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			convertView.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = convertView.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;// 等价于强制转成ImageView，TextView等
	}
}
/**
 * 在自定义的adapter中的getView方法中这么写：
 */
//
// public View getView(int position, View convertView, ViewGroup parent) {
//
// if (convertView == null) {
// convertView = LayoutInflater.from(context).inflate(
// R.layout.banana_phone, parent, false);
// }
//
// ImageView bananaView = ViewHolder.get(convertView, R.id.banana);
// TextView phoneView = ViewHolder.get(convertView, R.id.phone);
//
// BananaPhone bananaPhone = getItem(position);
// phoneView.setText(bananaPhone.getPhone());
// bananaView.setImageResource(bananaPhone.getBanana());
//
// return convertView;
// }