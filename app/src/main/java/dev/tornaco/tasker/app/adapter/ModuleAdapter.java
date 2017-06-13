package dev.tornaco.tasker.app.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.newstand.logger.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.tornaco.tasker.app.R;
import dev.tornaco.tasker.common.Consumer;
import dev.tornaco.tasker.test.Module;
import dev.tornaco.tasker.test.TestLauncher;
import dev.tornaco.tasker.utils.Collections;

/**
 * Created by Nick on 2017/6/13 13:55
 */

public class ModuleAdapter extends RecyclerView.Adapter<TwoLinesViewHolder> {

    private Context context;
    private final List<Module> modulePackageList;

    public ModuleAdapter(Context context) {
        this.context = context;
        this.modulePackageList = new ArrayList<>();
    }

    @Override
    public TwoLinesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.data_item_template_with_checkable, parent, false);
        final TwoLinesViewHolder holder = new TwoLinesViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(holder);
            }
        });
        return holder;
    }

    public void update(Collection<Module> src) {
        synchronized (modulePackageList) {
            modulePackageList.clear();
            Collections.consumeRemaining(src, new Consumer<Module>() {
                @Override
                public void accept(@Nullable Module module) {
                    if (module != null) modulePackageList.add(module);
                }
            });
        }
        triggerUpdate();
    }

    public void triggerUpdate() {
        notifyDataSetChanged();
    }

    private void onItemClick(TwoLinesViewHolder holder) {
        Module m = modulePackageList.get(holder.getAdapterPosition());
        TestLauncher.launchAsync(m);
    }

    @Override
    public void onBindViewHolder(TwoLinesViewHolder holder, int position) {
        Module modulePackage = modulePackageList.get(position);
        onBindViewHolder(holder, modulePackage);
    }

    protected void onBindViewHolder(TwoLinesViewHolder holder, final Module module) {
        Logger.d("onBindViewHolder:%s", module);
        holder.getLineOneTextView().setText(module.getTitle());
        holder.getLineTwoTextView().setText(module.getDescription());
    }

    @Override
    public int getItemCount() {
        return modulePackageList.size();
    }
}
