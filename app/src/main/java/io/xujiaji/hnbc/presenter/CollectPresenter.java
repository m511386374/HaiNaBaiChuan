package io.xujiaji.hnbc.presenter;

import java.util.List;

import io.xujiaji.hnbc.R;
import io.xujiaji.hnbc.contract.CollectContract;
import io.xujiaji.hnbc.fragment.base.BaseMainFragment;
import io.xujiaji.hnbc.fragment.base.BaseRefreshFragment;
import io.xujiaji.hnbc.model.data.DataFiller;
import io.xujiaji.hnbc.model.entity.Post;
import io.xujiaji.hnbc.model.entity.User;
import io.xujiaji.hnbc.model.net.NetRequest;
import io.xujiaji.xmvp.presenters.XBasePresenter;

/**
 * Created by jiana on 16-11-20.
 * 收藏
 */

public class CollectPresenter extends XBasePresenter<CollectContract.View> implements CollectContract.Presenter{

    private User user;

    public CollectPresenter(CollectContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        user = BaseMainFragment.getData(User.class.getSimpleName());
        if (isMe()) {
            view.showTitle(R.string.my_collect);
        } else {
            view.showTitle(R.string.collect);
        }
        requestUpdateListData();
    }

    @Override
    public void requestLoadListData(int nowSize) {
        NetRequest.Instance().pullCollectPost(user.getObjectId(), nowSize, BaseRefreshFragment.PAGE_SIZE, new NetRequest.RequestListener<List<Post>>() {
            @Override
            public void success(List<Post> posts) {
                if (posts == null || posts.size() == 0) {
                    view.loadListDateOver();
                } else {
                    view.loadListDataSuccess(posts);
                }
            }

            @Override
            public void error(String err) {
                view.loadListDataFail(err);
            }
        });
    }

    @Override
    public void requestUpdateListData() {
        NetRequest.Instance().pullCollectPost(user.getObjectId(), 0, BaseRefreshFragment.PAGE_SIZE, new NetRequest.RequestListener<List<Post>>() {
            @Override
            public void success(List<Post> posts) {
                view.updateListSuccess(posts);
            }

            @Override
            public void error(String err) {
                view.loadListDataFail(err);
            }
        });

    }

    @Override
    public void end() {
        super.end();
        user = null;
    }

    @Override
    public boolean isMe() {
        return DataFiller.getLocalUser().getObjectId().equals(user.getObjectId());
    }
}
