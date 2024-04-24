package com.example.moviereviewer.Adapters;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviereviewer.DataClasses.InstructionPage;
import com.example.moviereviewer.R;

import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<InstructionPage> instructionPages;

    public InstructionAdapter(Context context, List<InstructionPage> instructionPages) {
        this.inflater = LayoutInflater.from(context);
        this.instructionPages = instructionPages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.instruction_page_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InstructionPage instructionPage = instructionPages.get(position);

        holder.rvTextPageTitle.setText(instructionPage.getPageTitle());
        holder.rvTextPageDescription.setText(instructionPage.getPageDescription());
        holder.rvTextPageDescription.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public int getItemCount() {
        return instructionPages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView rvTextPageTitle, rvTextPageDescription;
        public ViewHolder(View view) {
            super(view);
            rvTextPageTitle = view.findViewById(R.id.rvTextPageTitle);
            rvTextPageDescription = view.findViewById(R.id.rvTextPageDescription);
        }
    }
}
