package com.example.currencyconverter.ui.vault.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R
import com.example.currencyconverter.data.model.LikedVault
import com.example.currencyconverter.databinding.FragmentVaultDateBinding
import com.example.currencyconverter.databinding.FragmentVaultItemBinding
import com.example.currencyconverter.ui.detail.DetailFragment
import com.example.currencyconverter.ui.vault.VaultAdapterInterface

class VaultAdapter(private val vaultInterface: VaultAdapterInterface) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewTypeDate = 0
    private val viewTypeContent = 1

    var vaultList = mutableListOf<LikedVault>()

    override fun getItemViewType(position: Int): Int {
        val dateHeader = 0
        return when (position) {
            dateHeader -> viewTypeDate
            else -> viewTypeContent
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val bindingMain = FragmentVaultItemBinding.inflate(inflater, parent, false)
        val bindingMinor = FragmentVaultDateBinding.inflate(inflater, parent, false)
        if (viewType == viewTypeDate)
            return RecyclerViewItem.VaultViewHolderMinor(bindingMinor, vaultInterface)

        return RecyclerViewItem.VaultViewHolderMain(bindingMain, vaultInterface)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is RecyclerViewItem.VaultViewHolderMinor) {
            holder.bindingMinor()
        }

        if (holder is RecyclerViewItem.VaultViewHolderMain) {
            val item = vaultList[position - 1]

            holder.itemView.setOnClickListener {
                vaultInterface.openDetailWindow()
                DetailFragment.setVault(item.name)
                DetailFragment.setRate(item.price)
                DetailFragment.getIco()[item.name]
                    ?.let { it1 -> DetailFragment.setCurrentIco(it1) }
            }
            holder.bindingMain(item)
        }
    }

    override fun getItemCount(): Int = vaultList.size + 1

    class RecyclerViewItem {
        class VaultViewHolderMinor(
            private val bindingMinor: FragmentVaultDateBinding,
            private val vaultInterface: VaultAdapterInterface,
        ) :
            RecyclerView.ViewHolder(bindingMinor.root) {

            fun bindingMinor() {
                vaultInterface.loadDate(bindingMinor)
                vaultInterface.loadTimestamp(bindingMinor)
            }
        }

        class VaultViewHolderMain(
            private val bindingMain: FragmentVaultItemBinding,
            private val vaultInterface: VaultAdapterInterface,
        ) :
            RecyclerView.ViewHolder(bindingMain.root) {
            private val valueIco = DetailFragment.getIco()
            fun bindingMain(item: LikedVault) {
                bindingMain.apply {
                    vaultIco.text = valueIco[item.name]
                    vaultName.text = item.name
                    vaultValue.text = item.price.toString()
                    if (item.is_liked) valueFavorite.setImageResource(R.drawable.ic_star_rate)
                    else valueFavorite.setImageResource(R.drawable.ic_base_star)

                    valueFavorite.setOnClickListener {
                        if (item.is_liked) vaultInterface.dislikeVault(item.name)
                        else vaultInterface.likeVault(item.name)
                    }
                }
            }
        }
    }
}