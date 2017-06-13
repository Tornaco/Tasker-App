package dev.tornaco.tasker.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.newstand.logger.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.tornaco.tasker.app.PackageModulesViewerActivity;
import dev.tornaco.tasker.app.R;
import dev.tornaco.tasker.protocal.Constants;
import dev.tornaco.tasker.test.ModulePackage;

/**
 * Created by Nick on 2017/6/13 13:55
 */

public class ModulePackageAdapter extends RecyclerView.Adapter<TwoLinesViewHolder> {

    private Context context;
    private final List<ModulePackage> modulePackageList;

    public ModulePackageAdapter(Context context) {
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

    public void update(Collection<ModulePackage> src) {
        synchronized (modulePackageList) {
            modulePackageList.clear();
            modulePackageList.addAll(src);
        }
        triggerUpdate();
    }

    public void triggerUpdate() {
        notifyDataSetChanged();
    }

    private void onItemClick(TwoLinesViewHolder holder) {
        Logger.d("onItemClick: %s", holder.getAdapterPosition());
        ModulePackage modulePackage = modulePackageList.get(holder.getAdapterPosition());
        Intent intent = new Intent(context, PackageModulesViewerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.INTENT_EXTRA_PACKAGE_NAME, modulePackage.getPkgName());
        context.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(TwoLinesViewHolder holder, int position) {
        ModulePackage modulePackage = modulePackageList.get(position);
        onBindViewHolder(holder, modulePackage);
    }

    protected void onBindViewHolder(TwoLinesViewHolder holder, final ModulePackage modulePackage) {
        holder.getLineOneTextView().setText(modulePackage.getPackageTitle());
        holder.getLineTwoTextView().setText(modulePackage.getPackageDescription());
    }

    @Override
    public int getItemCount() {
        return modulePackageList.size();
    }
}
