/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.creatures.ui

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.resources.MaterialResources.getDimensionPixelSize
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.CreaturesCardAdapter
import com.raywenderlich.android.creatures.model.CreatureStore.getCreatures

class AllFragment : Fragment() {

    private lateinit var creaturesRecyclerView: RecyclerView
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private lateinit var listItemDecor: SpacingItemDecoration
    private lateinit var gridItemDecor: SpacingItemDecoration
    private var spanState = SpanState.GRID

    private enum class SpanState {
        LIST, GRID
    }

    companion object {
        fun newInstance(): AllFragment {
            return AllFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spacing = resources.getDimensionPixelOffset(R.dimen.creature_card_grid_layout_margin)
        listItemDecor = SpacingItemDecoration(spacing, 1)
        gridItemDecor = SpacingItemDecoration(spacing, 2)

        creaturesRecyclerView = view.findViewById(R.id.creature_recycler_view)
        creaturesRecyclerView.adapter = CreaturesCardAdapter(getCreatures().toMutableList())
        layoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
        creaturesRecyclerView.layoutManager = layoutManager
        creaturesRecyclerView.addItemDecoration(gridItemDecor)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_span, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val listMenuButton = menu.findItem(R.id.action_span_1)
        val gridMenuButton = menu.findItem(R.id.action_span_2)

        when (spanState) {
            SpanState.GRID -> {
                gridMenuButton.isEnabled = false
                listMenuButton.isEnabled = true
            }

            SpanState.LIST -> {
                gridMenuButton.isEnabled = true
                listMenuButton.isEnabled = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_span_1 -> {
                spanState = SpanState.LIST
                updateRecyclerView(1, listItemDecor, gridItemDecor)
                return true
            }

            R.id.action_span_2 -> {
                spanState = SpanState.GRID
                updateRecyclerView(2, gridItemDecor, listItemDecor)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateRecyclerView(
        spanCount: Int,
        addDecor: SpacingItemDecoration,
        removeDecor: SpacingItemDecoration
    ) {
        layoutManager.spanCount = spanCount
        creaturesRecyclerView.removeItemDecoration(removeDecor)
        creaturesRecyclerView.addItemDecoration(addDecor)
    }
}