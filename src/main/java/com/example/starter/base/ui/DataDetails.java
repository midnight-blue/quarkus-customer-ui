package com.example.starter.base.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A condensed, read-only details card: an avatar/title header followed by a
 * tight grid of label/value rows. Build one with {@link #builder()} and drop
 * it into a detail pane (e.g. the detail slot of a {@code MasterDetailLayout})
 * wherever a compact summary of a selected record is needed.
 */
public class DataDetails extends Div {

    private DataDetails(Builder builder) {
        addClassNames("flex", "flex-col", "gap-3", "rounded-xl", "border", "border-grey-15",
                "bg-grey-0", "p-4", "h-fit");

        if (builder.avatarText != null || builder.title != null || builder.subtitle != null) {
            add(buildHeader(builder));
        }

        if (!builder.fields.isEmpty()) {
            add(buildFieldGrid(builder.fields));
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private static Div buildHeader(Builder builder) {
        Div header = new Div();
        header.addClassNames("flex", "items-center", "gap-3");

        if (builder.avatarText != null && !builder.avatarText.isBlank()) {
            Span avatar = new Span(builder.avatarText);
            avatar.addClassNames("flex", "items-center", "justify-center", "w-10", "h-10",
                    "rounded-full", "bg-brand-90", "text-grey-0", "font-semibold", "text-sm", "shrink-0");
            header.add(avatar);
        }

        Div titles = new Div();
        titles.addClassNames("flex", "flex-col", "min-w-0");
        if (builder.title != null) {
            Span title = new Span(builder.title);
            title.addClassNames("font-semibold", "text-grey-105", "truncate");
            titles.add(title);
        }
        if (builder.subtitle != null) {
            Span subtitle = new Span(builder.subtitle);
            subtitle.addClassNames("text-sm", "text-grey-60", "truncate");
            titles.add(subtitle);
        }
        header.add(titles);
        return header;
    }

    private static Div buildFieldGrid(List<Field> fields) {
        Div grid = new Div();
        grid.addClassNames("grid", "grid-cols-[auto_1fr]", "gap-x-3", "gap-y-1.5",
                "border-t", "border-grey-15", "pt-3");
        for (Field field : fields) {
            Span label = new Span(field.label());
            label.addClassNames("text-xs", "uppercase", "tracking-wide", "text-grey-60", "whitespace-nowrap");

            Span value = new Span(field.value());
            value.addClassNames("text-sm", "text-grey-100", "truncate");

            grid.add(label, value);
        }
        return grid;
    }

    private record Field(String label, String value) {
    }

    public static final class Builder implements Serializable {
        private final List<Field> fields = new ArrayList<>();
        private String avatarText;
        private String title;
        private String subtitle;

        private Builder() {
        }

        public Builder avatarText(String avatarText) {
            this.avatarText = avatarText;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder field(String label, String value) {
            this.fields.add(new Field(label, value));
            return this;
        }

        public DataDetails build() {
            return new DataDetails(this);
        }
    }
}
