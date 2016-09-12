/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tripin.com.tripin_shipper.adapter;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.activity.Activity_Dashboard;
import tripin.com.tripin_shipper.helper.ItemTouchHelperAdapter;
import tripin.com.tripin_shipper.helper.ItemTouchHelperViewHolder;
import tripin.com.tripin_shipper.helper.OnStartDragListener;
import tripin.com.tripin_shipper.model.AddressList;
import tripin.com.tripin_shipper.model.AddressObj;


/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class AddressSwapPickUpAdapter extends RecyclerView.Adapter<AddressSwapPickUpAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private static  List<AddressObj> mItems = new ArrayList<>();

    private final OnStartDragListener mDragStartListener;
    public static Activity_Dashboard mainActivity;

    public AddressSwapPickUpAdapter(Context context, OnStartDragListener dragStartListener, ArrayList<AddressObj> nameList) {
        mDragStartListener = dragStartListener;
        mItems = new ArrayList<>();
        mItems.addAll(nameList);
        mainActivity = (Activity_Dashboard) context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_pick_up_drop_swap_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.textView.setText(mItems.get(position).getName());
        holder.addressTextView.setText(mItems.get(position).getAddress());

        Log.e("dropped", "before dropped");

        // Start a drag whenever the handle view it touched
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(holder.itemView.getAlpha() > AddressList.diabledAlphaValue){
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                        Log.e("dropped", " touched ");
                        mainActivity.addressesFragment.swipeIndicationVg.setVisibility(View.GONE);

                    }
                }


                return false;
            }
        });

        /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("deleting", "is deleting " + position);
                AddressList.pickUpList.remove(position);
                mainActivity.addressesFragment.showOriginalContainers();
                mainActivity.addressesFragment.checkUserInputs();
                return true;
            }
        });
*/
        holder.textIdView.setText(""+(position + 1));

        if(AddressList.dropList.size() > 1 && mItems.size() == 1){
            holder.itemView.setAlpha(AddressList.diabledAlphaValue);
        }
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        Log.e("moved", " items moved " +fromPosition + " " +toPosition);

        return true;
    }




    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;
        public final TextView addressTextView;
        public final TextView textIdView;
        public final ViewGroup handleView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            addressTextView = (TextView) itemView.findViewById(R.id.addressText);
            handleView = (ViewGroup) itemView.findViewById(R.id.handle);
            textIdView  = (TextView) itemView.findViewById(R.id.textId);
        }

        @Override
        public void onItemSelected() {
            //itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            //itemView.setBackgroundColor(0);
            Log.e("itemCleared", "item has dropped");
           mainActivity.addressesFragment.setVisiblityOfBottomBtns(1);
            mainActivity.addressesFragment.listPickUpUpdated(mItems);
        }
    }
}
