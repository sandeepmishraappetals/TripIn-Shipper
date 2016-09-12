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

package tripin.com.tripin_shipper.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tripin.com.tripin_shipper.adapter.AddressSwapPickUpAdapter;
import tripin.com.tripin_shipper.helper.OnStartDragListener;
import tripin.com.tripin_shipper.helper.SimpleItemTouchHelperCallback;
import tripin.com.tripin_shipper.model.AddressList;


/**
 * @author Paul Burke (ipaulpro)
 */
public class AddressSwapPickUpFragment extends Fragment implements OnStartDragListener {

    private ItemTouchHelper mItemTouchHelper;
    private AddressSwapPickUpAdapter adapter;
    private RecyclerView recyclerView;


    public AddressSwapPickUpFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.setBackgroundColor(Color.WHITE);

        return new RecyclerView(container.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*        ArrayList<String> nameList = new ArrayList<>();
        nameList.add("one");
        nameList.add("two");
        nameList.add("three");*/
        adapter = new AddressSwapPickUpAdapter(getActivity(), this, AddressList.pickUpList);

        Log.e("AddingListInAdapter", ""+AddressList.pickUpList.size());

        recyclerView = (RecyclerView) view;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void updateList(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


}
