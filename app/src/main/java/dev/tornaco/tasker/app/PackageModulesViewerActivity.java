package dev.tornaco.tasker.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.newstand.logger.Logger;

import dev.tornaco.tasker.app.adapter.ModuleAdapter;
import dev.tornaco.tasker.common.Consumer;
import dev.tornaco.tasker.protocal.Constants;
import dev.tornaco.tasker.repo.ModulePackageLoader;
import dev.tornaco.tasker.test.ModulePackage;
import dev.tornaco.tasker.utils.ApkUtil;
import dev.tornaco.tasker.utils.SharedExecutor;

/**
 * Created by Nick on 2017/6/13 14:26
 */

public class PackageModulesViewerActivity extends AppCompatActivity {

    private String targetPackage;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ModuleAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolveIntent();
        setContentView(R.layout.recycler_view_template);
        setupView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLoading();
    }

    private void resolveIntent() {
        targetPackage = getIntent().getStringExtra(Constants.INTENT_EXTRA_PACKAGE_NAME);
        Logger.d("resolveIntent, package:%s", targetPackage);
        setTitle(ApkUtil.loadNameByPkgName(this, targetPackage));
    }

    private void setupView() {
        showHomeAsUp();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.polluted_waves));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = onCreateAdapter();
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoading();
            }
        });
    }

    protected void showHomeAsUp() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private ModuleAdapter onCreateAdapter() {
        return new ModuleAdapter(this);
    }

    private void startLoading() {
        ModulePackageLoader.loadModulePackageFromAsync(this, targetPackage, new Consumer<ModulePackage>() {
            @Override
            public void accept(@Nullable final ModulePackage modulePackage) {
                SharedExecutor.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        if (modulePackage != null) {
                            adapter.update(modulePackage.getModules());
                        }
                    }
                });
            }
        });
    }
}
