package edu.byu.cs.tweeter.client.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.presenter.presenter.FeedPresenter;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedFragment extends Fragment implements FeedPresenter.View {
    private static final String LOG_TAG = "FeedFragment";
    private static final String USER_KEY = "UserKey";
    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;
    private User user;
    private FeedRecyclerViewAdapter feedRecyclerViewAdapter;
    private FeedPresenter presenter;

    public static FeedFragment newInstance(User user) {
        FeedFragment fragment = new FeedFragment();

        Bundle args = new Bundle(1);
        args.putSerializable(USER_KEY, user);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        user = (User) getArguments().getSerializable(USER_KEY);

        RecyclerView feedRecyclerView = view.findViewById(R.id.feedRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        feedRecyclerView.setLayoutManager(layoutManager);

        feedRecyclerViewAdapter = new FeedRecyclerViewAdapter();
        feedRecyclerView.setAdapter(feedRecyclerViewAdapter);

        feedRecyclerView.addOnScrollListener(new FeedRecyclerViewPaginationScrollListener(layoutManager));

        presenter = new FeedPresenter(this);
        presenter.loadMoreItems(user);

        return view;
    }

    @Override
    public void setLoadingFooter(boolean value) {
        if (value) {
            feedRecyclerViewAdapter.addLoadingFooter();
        } else {
            feedRecyclerViewAdapter.removeLoadingFooter();
        }
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayError(String message) {}

    @Override
    public void addMoreItems(List<Status> statuses) {
        feedRecyclerViewAdapter.addItems(statuses);
    }

    @Override
    public void userSuccessful(User user) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, user);
        startActivity(intent);
    }

    private class FeedHolder extends RecyclerView.ViewHolder {
        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView post;
        private final TextView datetime;

        FeedHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.statusImage);
            userAlias = itemView.findViewById(R.id.statusAlias);
            userName = itemView.findViewById(R.id.statusName);
            post = itemView.findViewById(R.id.statusPost);
            datetime = itemView.findViewById(R.id.statusDatetime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.getUser(userAlias.getText().toString());
                }
            });
        }

        void bindStatus(Status status) {
            Picasso.get().load(status.getUser().getImageUrl()).into(userImage);
            userAlias.setText(status.getUser().getAlias());
            userName.setText(status.getUser().getName());
            datetime.setText(status.getDatetime());

            SpannableString spannableString = new SpannableString(status.getPost());

            for (String mention : status.getMentions()) {
                int startIndex = status.getPost().indexOf(mention);
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        TextView clickedMention = (TextView) widget;
                        Spanned s = (Spanned) clickedMention.getText();
                        int start = s.getSpanStart(this);
                        int end = s.getSpanEnd(this);

                        String clickable = s.subSequence(start, end).toString();

                        presenter.getUser(clickable);
                    }

                    @Override
                    public void updateDrawState(@NotNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                        ds.setUnderlineText(false);
                    }
                }, startIndex, (startIndex + mention.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (String url : status.getUrls()) {
                int startIndex = status.getPost().indexOf(url);
                spannableString.setSpan(new URLSpan(url), startIndex, (startIndex + url.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            post.setText(spannableString);
            post.setClickable(true);
            post.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedHolder> {
        private final List<Status> feed = new ArrayList<>();

        void addItems(List<Status> newStory) {
            int startInsertPosition = feed.size();
            feed.addAll(newStory);
            this.notifyItemRangeInserted(startInsertPosition, newStory.size());
        }

        void addItem(Status status) {
            feed.add(status);
            this.notifyItemInserted(feed.size() - 1);
        }

        void removeItem(Status status) {
            int position = feed.indexOf(status);
            feed.remove(position);
            this.notifyItemRemoved(position);
        }

        @NonNull
        @Override
        public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FeedFragment.this.getContext());
            View view;

            if (viewType == LOADING_DATA_VIEW) {
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new FeedHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FeedHolder feedHolder, int position) {
            if (!presenter.isLoading()) {
                feedHolder.bindStatus(feed.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return feed.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == feed.size() - 1 && presenter.isLoading()) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        void loadMoreItems() {
            presenter.loadMoreItems(user);

        }

        private void addLoadingFooter() {
            addItem(new Status("Dummy Post", new User("firstName", "lastName", "@coolAlias"), "2020-10-31 00:00:00", new ArrayList<String>() {{
                add("https://youtube.com");
            }}, new ArrayList<String>() {{
                add("@Dude1");
            }}));
        }

        private void removeLoadingFooter() {
            removeItem(feed.get(feed.size() - 1));
        }
    }

    private class FeedRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {
        private final LinearLayoutManager layoutManager;

        FeedRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!presenter.isLoading() && presenter.hasMorePages()) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> {
                        feedRecyclerViewAdapter.loadMoreItems();
                    }, 0);
                }
            }
        }
    }
}
