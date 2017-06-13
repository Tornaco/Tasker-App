package dev.tornaco.tasker.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dev.tornaco.tasker.app.R;
import dev.tornaco.tasker.app.adapter.ModulePackageAdapter;
import dev.tornaco.tasker.common.Consumer;
import dev.tornaco.tasker.repo.ModulePackageLoader;
import dev.tornaco.tasker.test.ModulePackage;
import dev.tornaco.tasker.utils.SharedExecutor;

/**
 * Created by Nick on 2017/6/13 13:47
 */

public class ModulePackageViewer extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ModulePackageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recycler_view_template, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.polluted_waves));
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();

        startLoading();
    }

    private void setupView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        adapter = onCreateAdapter();
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoading();
            }
        });
    }

    private ModulePackageAdapter onCreateAdapter() {
        return new ModulePackageAdapter(getActivity());
    }

    private void startLoading() {
        ModulePackageLoader.loadModulePackagesAsync(getActivity(), new Consumer<List<ModulePackage>>() {
            @Override
            public void accept(@NonNull final List<ModulePackage> modulePackages) {
                SharedExecutor.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        adapter.update(modulePackages);
                    }
                });
            }
        });
    }
}
