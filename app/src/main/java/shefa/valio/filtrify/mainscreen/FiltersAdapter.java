package shefa.valio.filtrify.mainscreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shefa.valio.filtrify.R;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.ViewHolder> {

    private List<String> filters;
    private ImageView imageView;

    public FiltersAdapter(List<String> filters) {
        this.filters = filters;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_filter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_filter) TextView textView;
        @BindView(R.id.img_view_item_filter) ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
