package tech.treeentertainment.camera;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import tech.treeentertainment.camera.view.TabletSessionFragment;
import tech.treeentertainment.camera.view.GalleryFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TabletSessionFragment(); // 세션 화면
            case 1:
                return new GalleryFragment(); // 갤러리 화면
            default:
                return new TabletSessionFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // 탭 개수 (Session, Gallery)
    }
}
