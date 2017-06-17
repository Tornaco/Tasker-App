package dev.tornaco.tasker.app.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.newstand.logger.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.tornaco.tasker.app.R;
import dev.tornaco.tasker.app.service.AbortSignal;
import dev.tornaco.tasker.app.service.InstrumentationLauncherServiceProxy;
import dev.tornaco.tasker.common.Consumer;
import dev.tornaco.tasker.test.Module;
import dev.tornaco.tasker.utils.Collections;
import dev.tornaco.tasker.utils.SharedExecutor;

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
        final Module m = modulePackageList.get(holder.getAdapterPosition());
        final AbortSignal signal = new AbortSignal();
        final ProgressDialog dialog = showRunningDialog(m, signal);
        SharedExecutor.execute(new Runnable() {
            @Override
            public void run() {
                InstrumentationLauncherServiceProxy proxy = new InstrumentationLauncherServiceProxy(context);
                try {
                    final String message = proxy.launch(m, signal);
                    SharedExecutor.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            showResultDialog("Execute message:" + message);
                        }
                    });
                } catch (final Exception e) {
                    SharedExecutor.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            showResultDialog("Fail execute:\n" + Logger.getStackTraceString(e));
                        }
                    });
                } finally {
                    SharedExecutor.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private ProgressDialog showRunningDialog(Module module, final AbortSignal signal) {
        ProgressDialog p = new ProgressDialog(context);
        p.setCancelable(false);
        p.setIndeterminate(true);
        p.setTitle("Executing");
        p.setMessage(module.getTitle());
        p.setButton(DialogInterface.BUTTON_NEGATIVE,
                context.getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signal.abort("User canceled");
                    }
                });
        p.show();
        return p;
    }

    private AlertDialog showResultDialog(String result) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(result)
                .setTitle("Result")
                .setPositiveButton(android.R.string.ok, null).create();
        alertDialog.show();
        return alertDialog;
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
