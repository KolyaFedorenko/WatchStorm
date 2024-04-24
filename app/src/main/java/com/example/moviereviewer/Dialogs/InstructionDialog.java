package com.example.moviereviewer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviereviewer.Adapters.InstructionAdapter;
import com.example.moviereviewer.DataClasses.InstructionPage;
import com.example.moviereviewer.R;

import java.util.ArrayList;

public class InstructionDialog extends CustomDialog {

    private RecyclerView dialogRecyclerViewInstruction;

    @Override
    public Dialog createDialog(Activity activity, boolean cancelable, int resource) {
        return super.createDialog(activity, cancelable, resource);
    }

    @Override
    public void findViews(Dialog dialog) {
        dialogRecyclerViewInstruction = dialog.findViewById(R.id.dialogRecyclerViewInstruction);
    }

    @Override
    public void useViews(Dialog dialog) {
        Context context = dialog.getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                dialog.getContext(),
                RecyclerView.HORIZONTAL,
                false
        );
        dialogRecyclerViewInstruction.setLayoutManager(layoutManager);
        ArrayList<InstructionPage> instructionPages = new ArrayList<>();
        addInstructionPages(instructionPages, context);
        InstructionAdapter adapter = new InstructionAdapter(context, instructionPages);
        dialogRecyclerViewInstruction.setAdapter(adapter);
        new PagerSnapHelper().attachToRecyclerView(dialogRecyclerViewInstruction);
    }

    private void addInstructionPages(ArrayList<InstructionPage> instructionPages, Context context) {

        InstructionPage addingMoviePage = createInstructionPage(
                context.getString(R.string.adding_a_movie),
                context.getString(R.string.adding_a_movie_info)
        );
        InstructionPage moviesListPage = createInstructionPage(
                context.getString(R.string.movies_list),
                context.getString(R.string.movies_list_info)
        );
        InstructionPage profilePage = createInstructionPage(
                context.getString(R.string.profile),
                context.getString(R.string.profile_info)
        );

        instructionPages.add(addingMoviePage);
        instructionPages.add(moviesListPage);
        instructionPages.add(profilePage);
    }

    private InstructionPage createInstructionPage(String title, String description) {
        return new InstructionPage(title, description);
    }
}
