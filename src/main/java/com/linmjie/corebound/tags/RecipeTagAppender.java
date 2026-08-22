package com.linmjie.corebound.tags;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class RecipeTagAppender {
    private final RecipeTagBuilder builder;

    RecipeTagAppender(RecipeTagBuilder builder) {
        this.builder = builder;
    }

    public RecipeTagAppender add(ResourceLocation recipe) {
        builder.add(recipe);
        return this;
    }

    public RecipeTagAppender addTag(RecipeTag tag) {
        builder.addTag(tag);
        return this;
    }

    public static final class RecipeTagBuilder {

        private final List<Entry> entries = new ArrayList<>();

        public void add(ResourceLocation id) {
            entries.add(new Entry(id, false, false));
        }

        public void addTag(RecipeTag tag) {
            entries.add(new Entry(tag.getId(), false, true));
        }

        public List<Entry> entries() {
            return List.copyOf(entries);
        }

        public record Entry(ResourceLocation id, boolean required, boolean tag) {}
    }
}
